const { Given, When, Then, BeforeAll, AfterAll, setDefaultTimeout } = require('@cucumber/cucumber')
const assert = require('node:assert/strict')
const { chromium } = require('playwright')

setDefaultTimeout(60 * 1000)

const FRONTEND_BASE_URL = process.env.FRONTEND_BASE_URL || 'http://127.0.0.1:4173'

let browser
let context
let page
let tasks

BeforeAll(async function () {
  browser = await chromium.launch({ headless: true })
})

AfterAll(async function () {
  if (context) {
    await context.close()
  }
  if (browser) {
    await browser.close()
  }
})

Given('que je charge l application frontend avec une API simulee', async function () {
  tasks = []
  context = await browser.newContext()
  page = await context.newPage()

  await page.route('**/api/tasks', async (route) => {
    if (route.request().method() === 'GET') {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify(tasks),
      })
      return
    }

    if (route.request().method() === 'POST') {
      const payload = JSON.parse(route.request().postData() || '{}')
      const created = {
        id: Date.now(),
        title: payload.title,
        description: payload.description || '',
        deadline: payload.deadline || null,
        estimatedMinutes: payload.estimatedMinutes || 30,
        importance: payload.importance || 'MEDIUM',
        urgency: payload.urgency || 'MEDIUM',
        type: payload.type || 'WORK',
        completed: false,
        priorityScore: 4,
      }
      tasks.unshift(created)
      await route.fulfill({
        status: 201,
        contentType: 'application/json',
        body: JSON.stringify(created),
      })
      return
    }

    await route.continue()
  })

  await page.route('**/api/planning/5days', async (route) => {
    await route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify({
        slots: [
          {
            start: '2026-06-30T09:00:00',
            end: '2026-06-30T10:00:00',
            type: 'WORK',
            taskId: null,
            taskTitle: null,
            allocatedMinutes: null,
          },
        ],
      }),
    })
  })

  await page.goto(FRONTEND_BASE_URL)
})

When('je saisis une nouvelle tache {string} de {int} minutes et je valide', async function (title, minutes) {
  await page.getByLabel('Titre').fill(title)
  await page.getByLabel('Description').fill('Scenario frontend')
  await page.getByLabel('Durée estimée (min)').fill(String(minutes))
  await page.getByRole('button', { name: 'Ajouter la tâche' }).click()
})

Then('je vois la tache {string} dans la liste priorisee', async function (title) {
  await page.locator('.task-list strong', { hasText: title }).first().waitFor({ state: 'visible' })
  const itemCount = await page.locator('.task-list li').count()
  assert.ok(itemCount >= 1)
})

Then('le statut affiche est {string}', async function (status) {
  await page.locator('.status-pill').filter({ hasText: status }).first().waitFor({ state: 'visible' })
})
