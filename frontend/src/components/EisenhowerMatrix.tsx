import { Fragment } from 'react'
import { IMPORTANCE_LEVELS, URGENCY_LEVELS, type Task } from '../types/task'

interface EisenhowerMatrixProps {
  tasks: Task[]
}

export function EisenhowerMatrix({ tasks }: EisenhowerMatrixProps) {
  return (
    <section className="panel">
      <div className="panel-header">
        <h2>Matrice d’Eisenhower 3 × 3</h2>
        <p>Classement automatique par urgence et importance.</p>
      </div>
      <div className="matrix-grid">
        <div className="matrix-corner" />
        {URGENCY_LEVELS.map((urgency) => (
          <div key={urgency} className="matrix-axis-label">
            Urgence {urgency.toLowerCase()}
          </div>
        ))}
        {IMPORTANCE_LEVELS.map((importance) => (
          <Fragment key={importance}>
            <div key={`${importance}-label`} className="matrix-axis-label importance-label">
              Importance {importance.toLowerCase()}
            </div>
            {URGENCY_LEVELS.map((urgency) => {
              const cellTasks = tasks.filter(
                (task) => task.importance === importance && task.urgency === urgency,
              )
              return (
                <div key={`${importance}-${urgency}`} className="matrix-cell">
                  <strong>{cellTasks.length} tâche(s)</strong>
                  <ul>
                    {cellTasks.map((task) => (
                      <li key={task.id}>
                        <span>{task.title}</span>
                        <small>Score {task.priorityScore}</small>
                      </li>
                    ))}
                  </ul>
                </div>
              )
            })}
          </Fragment>
        ))}
      </div>
    </section>
  )
}
