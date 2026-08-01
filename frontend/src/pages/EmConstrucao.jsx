import Box from '@mui/material/Box'
import Typography from '@mui/material/Typography'

/**
 * Placeholder de rota. Existe apenas para o roteamento ficar navegavel
 * enquanto as telas nao sao implementadas, e sera substituido por elas.
 */
export default function EmConstrucao({ titulo }) {
  return (
    <Box sx={{ py: 6, textAlign: 'center' }}>
      <Typography variant="h5" gutterBottom>
        {titulo}
      </Typography>
      <Typography variant="body2" color="text.secondary">
        Tela ainda nao implementada.
      </Typography>
    </Box>
  )
}
