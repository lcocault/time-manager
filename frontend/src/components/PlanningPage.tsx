import type { PlanningSlot } from '../types/task'

interface PlanningPageProps {
  slots: PlanningSlot[]
}

const formatter = new Intl.DateTimeFormat('fr-FR', {
  weekday: 'short',
  day: '2-digit',
  month: '2-digit',
  hour: '2-digit',
  minute: '2-digit',
})

export function PlanningPage({ slots }: PlanningPageProps) {
  return (
    <section className="panel">
      <div className="panel-header">
        <h2>Planning intelligent sur 5 jours</h2>
        <p>Créneaux disponibles synchronisés et affectation automatique des tâches.</p>
      </div>
      <div className="planning-list">
        {slots.map((slot) => (
          <article key={`${slot.start}-${slot.type}`} className="planning-card">
            <header>
              <span>{formatter.format(new Date(slot.start))}</span>
              <strong>{slot.type === 'WORK' ? 'Pro' : 'Perso'}</strong>
            </header>
            <p>
              {formatter.format(new Date(slot.start))} → {new Intl.DateTimeFormat('fr-FR', {
                hour: '2-digit',
                minute: '2-digit',
              }).format(new Date(slot.end))}
            </p>
            <div className="planning-task">
              <strong>{slot.taskTitle ?? 'Créneau libre'}</strong>
              <span>
                {slot.allocatedMinutes
                  ? `${slot.allocatedMinutes} min réservées`
                  : 'Disponible pour une nouvelle tâche'}
              </span>
            </div>
          </article>
        ))}
      </div>
    </section>
  )
}
