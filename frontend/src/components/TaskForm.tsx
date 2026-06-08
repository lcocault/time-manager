import { useMemo, useState, type FormEvent } from 'react'
import type { CreateTaskInput, Importance, TaskType, Urgency } from '../types/task'

interface TaskFormProps {
  onSubmit: (task: CreateTaskInput) => Promise<void>
}

const defaultTask: CreateTaskInput = {
  title: '',
  description: '',
  deadline: null,
  estimatedMinutes: 60,
  importance: 'MEDIUM',
  urgency: 'MEDIUM',
  type: 'WORK',
}

export function TaskForm({ onSubmit }: TaskFormProps) {
  const [task, setTask] = useState<CreateTaskInput>(defaultTask)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [errorMessage, setErrorMessage] = useState<string | null>(null)
  const options = useMemo(
    () => ({
      importance: ['HIGH', 'MEDIUM', 'LOW'] as Importance[],
      urgency: ['HIGH', 'MEDIUM', 'LOW'] as Urgency[],
      type: ['WORK', 'PERSONAL'] as TaskType[],
    }),
    [],
  )

  const updateField = <K extends keyof CreateTaskInput>(field: K, value: CreateTaskInput[K]) => {
    setTask((currentTask) => ({ ...currentTask, [field]: value }))
  }

  const submit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if (!task.title.trim()) {
      setErrorMessage('Le titre est obligatoire.')
      return
    }
    setIsSubmitting(true)
    setErrorMessage(null)
    try {
      await onSubmit(task)
      setTask(defaultTask)
    } catch {
      setErrorMessage('Impossible d’enregistrer la tâche pour le moment.')
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <form className="panel task-form" onSubmit={submit}>
      <div className="panel-header">
        <h2>Nouvelle tâche</h2>
        <p>Crée une tâche typée pour la matrice d’Eisenhower et la planification.</p>
      </div>
      <label>
        Titre
        <input value={task.title} onChange={(event) => updateField('title', event.target.value)} />
      </label>
      <label>
        Description
        <textarea
          rows={3}
          value={task.description}
          onChange={(event) => updateField('description', event.target.value)}
        />
      </label>
      <div className="grid two-columns">
        <label>
          Deadline
          <input
            type="datetime-local"
            value={task.deadline ?? ''}
            onChange={(event) => updateField('deadline', event.target.value || null)}
          />
        </label>
        <label>
          Durée estimée (min)
          <input
            type="number"
            min={15}
            step={15}
            value={task.estimatedMinutes}
            onChange={(event) => updateField('estimatedMinutes', Number(event.target.value))}
          />
        </label>
      </div>
      <div className="grid three-columns">
        <label>
          Importance
          <select
            value={task.importance}
            onChange={(event) => updateField('importance', event.target.value as Importance)}
          >
            {options.importance.map((importance) => (
              <option key={importance} value={importance}>
                {importance}
              </option>
            ))}
          </select>
        </label>
        <label>
          Urgence
          <select value={task.urgency} onChange={(event) => updateField('urgency', event.target.value as Urgency)}>
            {options.urgency.map((urgency) => (
              <option key={urgency} value={urgency}>
                {urgency}
              </option>
            ))}
          </select>
        </label>
        <label>
          Type
          <select value={task.type} onChange={(event) => updateField('type', event.target.value as TaskType)}>
            {options.type.map((type) => (
              <option key={type} value={type}>
                {type}
              </option>
            ))}
          </select>
        </label>
      </div>
      {errorMessage ? <p className="error-message">{errorMessage}</p> : null}
      <button type="submit" disabled={isSubmitting}>
        {isSubmitting ? 'Enregistrement...' : 'Ajouter la tâche'}
      </button>
    </form>
  )
}
