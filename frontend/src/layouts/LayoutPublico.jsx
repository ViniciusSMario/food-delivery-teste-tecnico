import Box from '@mui/material/Box'
import Container from '@mui/material/Container'
import Paper from '@mui/material/Paper'
import Typography from '@mui/material/Typography'
import { Outlet } from 'react-router-dom'

/** Layout das rotas abertas (login e cadastro): cartao centralizado, sem barra. */
export default function LayoutPublico() {
  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        bgcolor: 'background.default',
      }}
    >
      <Container maxWidth="xs">
        <Typography variant="h5" component="h1" align="center" gutterBottom>
          Food Delivery
        </Typography>
        <Paper elevation={2} sx={{ p: 3 }}>
          <Outlet />
        </Paper>
      </Container>
    </Box>
  )
}
