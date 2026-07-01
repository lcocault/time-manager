import type { Risk } from '../types/risk'

interface RiskRegisterProps {
  risks: Risk[]
}

function displayImpact(risk: Risk, source: 'initial' | 'mitigated') {
  if (risk.measurementMode === 'SCALE') {
    return source === 'initial'
      ? `Impact ${risk.initialImpactScale}/5`
      : `Impact ${risk.mitigatedImpactScale}/5`
  }
  return source === 'initial'
    ? `Impact ${risk.initialImpactEuros?.toLocaleString('fr-FR')} EUR`
    : `Impact ${risk.mitigatedImpactEuros?.toLocaleString('fr-FR')} EUR`
}

function displayProbability(risk: Risk, source: 'initial' | 'corrected') {
  if (risk.measurementMode === 'SCALE') {
    return source === 'initial'
      ? `Proba ${risk.initialProbabilityScale}/5`
      : `Proba ${risk.correctedProbabilityScale}/5`
  }
  return source === 'initial'
    ? `Proba ${risk.initialProbabilityValue?.toFixed(2)}`
    : `Proba ${risk.correctedProbabilityValue?.toFixed(2)}`
}

export function RiskRegister({ risks }: RiskRegisterProps) {
  return (
    <section className="panel risk-register">
      <div className="panel-header">
        <h2>Registre des risques</h2>
        <p>Suivi des risques, des actions et des valeurs avant/après mitigation.</p>
      </div>
      <ul className="risk-list">
        {risks.map((risk) => (
          <li key={risk.id}>
            <div>
              <strong>Risque #{risk.id}</strong>
              <p>{risk.riskFactor}</p>
              <p>{risk.consequences}</p>
            </div>
            <div className="risk-details">
              <span>{risk.impactNature}</span>
              <span>Action #{risk.actionId}: {risk.action}</span>
              <span>{risk.actionNature} / {risk.owner}</span>
              <span>{displayImpact(risk, 'initial')} - {displayProbability(risk, 'initial')}</span>
              <span>{displayImpact(risk, 'mitigated')} - {displayProbability(risk, 'corrected')}</span>
            </div>
          </li>
        ))}
      </ul>
    </section>
  )
}