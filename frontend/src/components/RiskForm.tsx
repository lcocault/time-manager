import { useState, type FormEvent } from 'react'
import {
  RISK_ACTION_NATURES,
  RISK_MEASUREMENT_MODES,
  RISK_OWNERS,
  type CreateRiskInput,
  type RiskActionNature,
  type RiskMeasurementMode,
  type RiskOwner,
} from '../types/risk'

interface RiskFormProps {
  onSubmit: (risk: CreateRiskInput) => Promise<void>
}

const defaultRisk: CreateRiskInput = {
  riskFactor: '',
  consequences: '',
  impactNature: '',
  measurementMode: 'SCALE',
  initialImpactScale: 3,
  initialImpactEuros: null,
  initialProbabilityScale: 3,
  initialProbabilityValue: null,
  actionId: 1,
  action: '',
  actionNature: 'BOTH',
  owner: 'BOTH',
  mitigatedImpactScale: 2,
  mitigatedImpactEuros: null,
  correctedProbabilityScale: 2,
  correctedProbabilityValue: null,
}

export function RiskForm({ onSubmit }: RiskFormProps) {
  const [risk, setRisk] = useState<CreateRiskInput>(defaultRisk)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [errorMessage, setErrorMessage] = useState<string | null>(null)

  const updateField = <K extends keyof CreateRiskInput>(field: K, value: CreateRiskInput[K]) => {
    setRisk((currentRisk) => ({ ...currentRisk, [field]: value }))
  }

  const switchMode = (mode: RiskMeasurementMode) => {
    setRisk((currentRisk) => {
      if (mode === 'SCALE') {
        return {
          ...currentRisk,
          measurementMode: mode,
          initialImpactScale: currentRisk.initialImpactScale ?? 3,
          initialProbabilityScale: currentRisk.initialProbabilityScale ?? 3,
          mitigatedImpactScale: currentRisk.mitigatedImpactScale ?? 2,
          correctedProbabilityScale: currentRisk.correctedProbabilityScale ?? 2,
          initialImpactEuros: null,
          initialProbabilityValue: null,
          mitigatedImpactEuros: null,
          correctedProbabilityValue: null,
        }
      }
      return {
        ...currentRisk,
        measurementMode: mode,
        initialImpactScale: null,
        initialProbabilityScale: null,
        mitigatedImpactScale: null,
        correctedProbabilityScale: null,
        initialImpactEuros: currentRisk.initialImpactEuros ?? 10000,
        initialProbabilityValue: currentRisk.initialProbabilityValue ?? 0.5,
        mitigatedImpactEuros: currentRisk.mitigatedImpactEuros ?? 5000,
        correctedProbabilityValue: currentRisk.correctedProbabilityValue ?? 0.3,
      }
    })
  }

  const submit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if (!risk.riskFactor.trim() || !risk.consequences.trim() || !risk.impactNature.trim() || !risk.action.trim()) {
      setErrorMessage('Merci de compléter les champs textuels obligatoires.')
      return
    }
    if (risk.actionId <= 0) {
      setErrorMessage('Id Action doit être supérieur à 0.')
      return
    }

    setErrorMessage(null)
    setIsSubmitting(true)
    try {
      await onSubmit(risk)
      setRisk(defaultRisk)
    } catch {
      setErrorMessage('Impossible d’enregistrer le risque pour le moment.')
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <form className="panel task-form" onSubmit={submit}>
      <div className="panel-header">
        <h2>Nouveau risque</h2>
        <p>Qualification du risque et action associée au module TimeManager.</p>
      </div>

      <div className="grid two-columns">
        <label>
          Facteur de risque
          <input value={risk.riskFactor} onChange={(event) => updateField('riskFactor', event.target.value)} />
        </label>
        <label>
          Nature de l’impact
          <input value={risk.impactNature} onChange={(event) => updateField('impactNature', event.target.value)} />
        </label>
      </div>

      <label>
        Conséquences
        <textarea
          rows={3}
          value={risk.consequences}
          onChange={(event) => updateField('consequences', event.target.value)}
        />
      </label>

      <div className="grid two-columns">
        <label>
          Id Action (TimeManager)
          <input
            type="number"
            min={1}
            value={risk.actionId}
            onChange={(event) => updateField('actionId', Number(event.target.value))}
          />
        </label>
        <label>
          Action
          <input value={risk.action} onChange={(event) => updateField('action', event.target.value)} />
        </label>
      </div>

      <div className="grid three-columns">
        <label>
          Nature de l’action
          <select
            value={risk.actionNature}
            onChange={(event) => updateField('actionNature', event.target.value as RiskActionNature)}
          >
            {RISK_ACTION_NATURES.map((nature) => (
              <option key={nature} value={nature}>
                {nature}
              </option>
            ))}
          </select>
        </label>
        <label>
          Porteur
          <select value={risk.owner} onChange={(event) => updateField('owner', event.target.value as RiskOwner)}>
            {RISK_OWNERS.map((owner) => (
              <option key={owner} value={owner}>
                {owner}
              </option>
            ))}
          </select>
        </label>
        <label>
          Mode de mesure
          <select
            value={risk.measurementMode}
            onChange={(event) => switchMode(event.target.value as RiskMeasurementMode)}
          >
            {RISK_MEASUREMENT_MODES.map((mode) => (
              <option key={mode} value={mode}>
                {mode}
              </option>
            ))}
          </select>
        </label>
      </div>

      {risk.measurementMode === 'SCALE' ? (
        <div className="grid two-columns">
          <label>
            Impact initial (1-5)
            <input
              type="number"
              min={1}
              max={5}
              value={risk.initialImpactScale ?? 3}
              onChange={(event) => updateField('initialImpactScale', Number(event.target.value))}
            />
          </label>
          <label>
            Proba initiale (1-5)
            <input
              type="number"
              min={1}
              max={5}
              value={risk.initialProbabilityScale ?? 3}
              onChange={(event) => updateField('initialProbabilityScale', Number(event.target.value))}
            />
          </label>
          <label>
            Impact mitigé (1-5)
            <input
              type="number"
              min={1}
              max={5}
              value={risk.mitigatedImpactScale ?? 2}
              onChange={(event) => updateField('mitigatedImpactScale', Number(event.target.value))}
            />
          </label>
          <label>
            Proba corrigée (1-5)
            <input
              type="number"
              min={1}
              max={5}
              value={risk.correctedProbabilityScale ?? 2}
              onChange={(event) => updateField('correctedProbabilityScale', Number(event.target.value))}
            />
          </label>
        </div>
      ) : (
        <div className="grid two-columns">
          <label>
            Impact initial (EUR)
            <input
              type="number"
              min={0}
              step={100}
              value={risk.initialImpactEuros ?? 0}
              onChange={(event) => updateField('initialImpactEuros', Number(event.target.value))}
            />
          </label>
          <label>
            Proba initiale (0-1)
            <input
              type="number"
              min={0}
              max={1}
              step={0.01}
              value={risk.initialProbabilityValue ?? 0}
              onChange={(event) => updateField('initialProbabilityValue', Number(event.target.value))}
            />
          </label>
          <label>
            Impact mitigé (EUR)
            <input
              type="number"
              min={0}
              step={100}
              value={risk.mitigatedImpactEuros ?? 0}
              onChange={(event) => updateField('mitigatedImpactEuros', Number(event.target.value))}
            />
          </label>
          <label>
            Proba corrigée (0-1)
            <input
              type="number"
              min={0}
              max={1}
              step={0.01}
              value={risk.correctedProbabilityValue ?? 0}
              onChange={(event) => updateField('correctedProbabilityValue', Number(event.target.value))}
            />
          </label>
        </div>
      )}

      {errorMessage ? <p className="error-message">{errorMessage}</p> : null}
      <button type="submit" disabled={isSubmitting}>
        {isSubmitting ? 'Enregistrement...' : 'Ajouter le risque'}
      </button>
    </form>
  )
}