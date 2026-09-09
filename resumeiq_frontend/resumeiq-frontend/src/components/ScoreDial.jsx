function scoreTone(score) {
  if (score >= 75) return { color: 'var(--pass)', bg: 'var(--pass-bg)' }
  if (score >= 50) return { color: 'var(--warn)', bg: 'var(--warn-bg)' }
  return { color: 'var(--gap)', bg: 'var(--gap-bg)' }
}

export default function ScoreDial({ label, score, size = 96 }) {
  const tone = scoreTone(score ?? 0)
  const circumference = 2 * Math.PI * 42
  const offset = circumference - ((score ?? 0) / 100) * circumference

  return (
    <div className="score-dial" style={{ width: size }}>
      <svg viewBox="0 0 100 100" width={size} height={size}>
        <circle cx="50" cy="50" r="42" fill="none" stroke="var(--line)" strokeWidth="8" />
        <circle
          cx="50"
          cy="50"
          r="42"
          fill="none"
          stroke={tone.color}
          strokeWidth="8"
          strokeLinecap="round"
          strokeDasharray={circumference}
          strokeDashoffset={offset}
          transform="rotate(-90 50 50)"
        />
        <text
          x="50"
          y="54"
          textAnchor="middle"
          fontFamily="Lora, serif"
          fontSize="22"
          fontWeight="600"
          fill="var(--ink)"
        >
          {score ?? '—'}
        </text>
      </svg>
      <div className="score-dial__label">{label}</div>

      <style>{`
        .score-dial {
          display: flex;
          flex-direction: column;
          align-items: center;
          gap: 0.4em;
        }
        .score-dial__label {
          font-size: 0.78rem;
          font-weight: 600;
          color: var(--ink-soft);
          text-align: center;
        }
      `}</style>
    </div>
  )
}
