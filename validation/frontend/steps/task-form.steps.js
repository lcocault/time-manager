const { Given, When, Then, BeforeAll, AfterAll, setDefaultTimeout } = require('@cucumber/cucumber')
const assert = require('node:assert/strict')
const { chromium } = require('playwright')

setDefaultTimeout(60 * 1000)

const FRONTEND_BASE_URL = process.env.FRONTEND_BASE_URL || 'http://127.0.0.1:4173'

let browser
let context
let page
let tasks
let risks

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
  risks = []
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

  await page.route('**/api/risks', async (route) => {
    if (route.request().method() === 'GET') {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify(risks),
      })
      return
    }

    if (route.request().method() === 'POST') {
      const payload = JSON.parse(route.request().postData() || '{}')
      const created = {
        id: Date.now(),
        riskFactor: payload.riskFactor,
        consequences: payload.consequences,
        impactNature: payload.impactNature,
        measurementMode: payload.measurementMode || 'SCALE',
        initialImpactScale: payload.initialImpactScale ?? 3,
        initialImpactEuros: payload.initialImpactEuros ?? null,
        initialProbabilityScale: payload.initialProbabilityScale ?? 3,
        initialProbabilityValue: payload.initialProbabilityValue ?? null,
        actionId: payload.actionId ?? 1,
        action: payload.action,
        actionNature: payload.actionNature || 'BOTH',
        owner: payload.owner || 'BOTH',
        mitigatedImpactScale: payload.mitigatedImpactScale ?? 2,
        mitigatedImpactEuros: payload.mitigatedImpactEuros ?? null,
        correctedProbabilityScale: payload.correctedProbabilityScale ?? 2,
        correctedProbabilityValue: payload.correctedProbabilityValue ?? null,
      }
      risks.unshift(created)
      await route.fulfill({
        status: 201,
        contentType: 'application/json',
        body: JSON.stringify(created),
      })
      return
    }

    await route.continue()
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

When('je saisis un nouveau risque {string} et je valide', async function (riskFactor) {
  await page.getByLabel('Facteur de risque').fill(riskFactor)
  await page.getByLabel('Nature de l’impact').fill('Impact planning')
  await page.getByLabel('Conséquences').fill('Risque de glissement de planning')
  await page.getByLabel('Id Action (TimeManager)').fill('42')
  await page.getByRole('textbox', { name: 'Action' }).fill('Ajouter une action preventive')
  await page.getByRole('button', { name: 'Ajouter le risque' }).click()
})

Then('je vois le risque {string} dans le registre', async function (riskFactor) {
  await page.locator('.risk-list li p', { hasText: riskFactor }).first().waitFor({ state: 'visible' })
  const itemCount = await page.locator('.risk-list li').count()
  assert.ok(itemCount >= 1)
})

Then('je vois l action de risque {string}', async function (actionName) {
  await page.locator('.risk-details span', { hasText: actionName }).first().waitFor({ state: 'visible' })
})
