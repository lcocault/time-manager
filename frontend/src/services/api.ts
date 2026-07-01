import type { CreateTaskInput, PlanningResponse, Task } from '../types/task'
import type { CreateRiskInput, Risk } from '../types/risk'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api'

async function parseJson<T>(response: Response): Promise<T> {
  if (!response.ok) {
    throw new Error(`API request failed with status ${response.status}`)
  }
  return (await response.json()) as T
}

export async function listTasks(): Promise<Task[]> {
  const response = await fetch(`${API_BASE_URL}/tasks`)
  return parseJson<Task[]>(response)
}

export async function createTask(input: CreateTaskInput): Promise<Task> {
  const response = await fetch(`${API_BASE_URL}/tasks`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  })
  return parseJson<Task>(response)
}

export async function getFiveDayPlanning(): Promise<PlanningResponse> {
  const response = await fetch(`${API_BASE_URL}/planning/5days`)
  return parseJson<PlanningResponse>(response)
}

export async function listRisks(): Promise<Risk[]> {
  const response = await fetch(`${API_BASE_URL}/risks`)
  return parseJson<Risk[]>(response)
}

export async function createRisk(input: CreateRiskInput): Promise<Risk> {
  const response = await fetch(`${API_BASE_URL}/risks`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  })
  return parseJson<Risk>(response)
}
