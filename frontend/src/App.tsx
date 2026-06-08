import { useEffect, useMemo, useState } from 'react'
import './App.css'
import { EisenhowerMatrix } from './components/EisenhowerMatrix'
import { PlanningPage } from './components/PlanningPage'
import { TaskForm } from './components/TaskForm'
import { createTask, getFiveDayPlanning, listTasks } from './services/api'
import type { CreateTaskInput, PlanningSlot, Task } from './types/task'

const fallbackTasks: Task[] = [
  {
    id: 1,
    title: 'Préparer la roadmap',
    description: 'Aligner les priorités du sprint.',
    deadline: new Date().toISOString(),
    estimatedMinutes: 120,
    importance: 'HIGH',
    urgency: 'HIGH',
    type: 'WORK',
    completed: false,
    priorityScore: 9,
  },
  {
    id: 2,
    title: 'Réserver séance de sport',
    description: 'Garder un créneau personnel.',
    deadline: null,
    estimatedMinutes: 60,
    importance: 'MEDIUM',
    urgency: 'MEDIUM',
    type: 'PERSONAL',
    completed: false,
    priorityScore: 4,
  },
]

const fallbackPlanning: PlanningSlot[] = [
  {
    start: new Date().toISOString(),
    end: new Date(Date.now() + 2 * 60 * 60 * 1000).toISOString(),
    type: 'WORK',
    taskId: 1,
    taskTitle: 'Préparer la roadmap',
    allocatedMinutes: 120,
  },
  {
    start: new Date(Date.now() + 6 * 60 * 60 * 1000).toISOString(),
    end: new Date(Date.now() + 8 * 60 * 60 * 1000).toISOString(),
    type: 'PERSONAL',
    taskId: 2,
    taskTitle: 'Réserver séance de sport',
    allocatedMinutes: 60,
  },
]

function computePriorityScore(task: CreateTaskInput) {
  const weight = { LOW: 1, MEDIUM: 2, HIGH: 3 } as const
  return weight[task.importance] * weight[task.urgency]
}

function App() {
  const [tasks, setTasks] = useState<Task[]>([])
  const [slots, setSlots] = useState<PlanningSlot[]>([])
  const [status, setStatus] = useState('Chargement des données...')

  useEffect(() => {
    const loadData = async () => {
      try {
        const [loadedTasks, planning] = await Promise.all([listTasks(), getFiveDayPlanning()])
        setTasks(loadedTasks)
        setSlots(planning.slots)
        setStatus('API connectée')
      } catch {
        setTasks(fallbackTasks)
        setSlots(fallbackPlanning)
        setStatus('Mode démonstration (backend indisponible)')
      }
    }

    void loadData()
  }, [])

  const sortedTasks = useMemo(
    () => [...tasks].sort((left, right) => right.priorityScore - left.priorityScore),
    [tasks],
  )

  const handleCreateTask = async (input: CreateTaskInput) => {
    try {
      const createdTask = await createTask(input)
      setTasks((currentTasks) => [createdTask, ...currentTasks])
      const planning = await getFiveDayPlanning()
      setSlots(planning.slots)
      setStatus('API connectée')
    } catch {
      const localTask: Task = {
        id: Date.now(),
        ...input,
        description: input.description,
        completed: false,
        priorityScore: computePriorityScore(input),
      }
      setTasks((currentTasks) => [localTask, ...currentTasks])
      setStatus('Mode démonstration (backend indisponible)')
    }
  }

  return (
    <main className="app-shell">
      <header className="hero-banner panel">
        <div>
          <p className="eyebrow">Time Manager personnel</p>
          <h1>Organisation intelligente des tâches et du temps</h1>
          <p>
            Priorisation Eisenhower 3 × 3, synchronisation des créneaux pro/perso et planning sur 5 jours.
          </p>
        </div>
        <span className="status-pill">{status}</span>
      </header>
      <section className="layout-grid">
        <TaskForm onSubmit={handleCreateTask} />
        <section className="panel task-list-panel">
          <div className="panel-header">
            <h2>Tâches priorisées</h2>
            <p>Triées automatiquement selon le score urgence × importance.</p>
          </div>
          <ul className="task-list">
            {sortedTasks.map((task) => (
              <li key={task.id}>
                <div>
                  <strong>{task.title}</strong>
                  <p>{task.description || 'Aucune description'}</p>
                </div>
                <span>Score {task.priorityScore}</span>
              </li>
            ))}
          </ul>
        </section>
      </section>
      <EisenhowerMatrix tasks={sortedTasks} />
      <PlanningPage slots={slots} />
    </main>
  )
}

export default App
