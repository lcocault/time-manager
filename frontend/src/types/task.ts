export const IMPORTANCE_LEVELS = ['HIGH', 'MEDIUM', 'LOW'] as const
export const URGENCY_LEVELS = ['HIGH', 'MEDIUM', 'LOW'] as const

export type Importance = (typeof IMPORTANCE_LEVELS)[number]
export type Urgency = (typeof URGENCY_LEVELS)[number]
export type TaskType = 'WORK' | 'PERSONAL'

export interface Task {
  id: number
  title: string
  description: string
  deadline: string | null
  estimatedMinutes: number
  importance: Importance
  urgency: Urgency
  type: TaskType
  completed: boolean
  priorityScore: number
}

export interface CreateTaskInput {
  title: string
  description: string
  deadline: string | null
  estimatedMinutes: number
  importance: Importance
  urgency: Urgency
  type: TaskType
}

export interface PlanningSlot {
  start: string
  end: string
  type: TaskType
  taskId: number | null
  taskTitle: string | null
  allocatedMinutes: number | null
}

export interface PlanningResponse {
  slots: PlanningSlot[]
}
