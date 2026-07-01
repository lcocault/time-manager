export const RISK_MEASUREMENT_MODES = ['SCALE', 'QUANTITATIVE'] as const
export const RISK_ACTION_NATURES = ['AVOID', 'MITIGATE', 'BOTH'] as const
export const RISK_OWNERS = ['CNES', 'CAPGEMINI', 'BOTH'] as const

export type RiskMeasurementMode = (typeof RISK_MEASUREMENT_MODES)[number]
export type RiskActionNature = (typeof RISK_ACTION_NATURES)[number]
export type RiskOwner = (typeof RISK_OWNERS)[number]

export interface Risk {
  id: number
  riskFactor: string
  consequences: string
  impactNature: string
  measurementMode: RiskMeasurementMode
  initialImpactScale: number | null
  initialImpactEuros: number | null
  initialProbabilityScale: number | null
  initialProbabilityValue: number | null
  actionId: number
  action: string
  actionNature: RiskActionNature
  owner: RiskOwner
  mitigatedImpactScale: number | null
  mitigatedImpactEuros: number | null
  correctedProbabilityScale: number | null
  correctedProbabilityValue: number | null
}

export interface CreateRiskInput {
  riskFactor: string
  consequences: string
  impactNature: string
  measurementMode: RiskMeasurementMode
  initialImpactScale: number | null
  initialImpactEuros: number | null
  initialProbabilityScale: number | null
  initialProbabilityValue: number | null
  actionId: number
  action: string
  actionNature: RiskActionNature
  owner: RiskOwner
  mitigatedImpactScale: number | null
  mitigatedImpactEuros: number | null
  correctedProbabilityScale: number | null
  correctedProbabilityValue: number | null
}