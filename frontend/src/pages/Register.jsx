import Alert from '@mui/material/Alert'
import Box from '@mui/material/Box'
import Button from '@mui/material/Button'
import CircularProgress from '@mui/material/CircularProgress'
import Stack from '@mui/material/Stack'
import TextField from '@mui/material/TextField'
import Typography from '@mui/material/Typography'
import { useState } from 'react'
import { Link as RouterLink, useLocation, useNavigate } from 'react-router-dom'
import { register } from '../services/auth'
import { salvarToken } from '../services/token'

export default function Register() {
  const navigate = useNavigate()
  const location = useLocation()

  const [nome, setNome] = useState('')
  const [email, setEmail] = useState('')
  const [senha, setSenha] = useState('')
  const [carregando, setCarregando] = useState(false)
  const [erro, setErro] = useState(null)

  async function aoSubmeter(evento) {
    evento.preventDefault()
    setErro(null)
    setCarregando(true)

    try {
      const { token } = await register({ nome, email, senha })
      salvarToken(token)

      const destino = location.state?.de ?? '/pedidos'
      navigate(destino, { replace: true })
    } catch (excecao) {
      const mensagem = excecao.response?.data?.mensagem ?? 'Nao foi possivel realizar o cadastro. Tente novamente.'
      setErro(mensagem)
    } finally {
      setCarregando(false)
    }
  }

  return (
    <Box component="form" onSubmit={aoSubmeter} noValidate>
      <Stack spacing={2}>
        {erro && <Alert severity="error">{erro}</Alert>}

        <TextField
          label="Nome"
          type="text"
          value={nome}
          onChange={(evento) => setNome(evento.target.value)}
          required
          autoFocus
          fullWidth
        />

        <TextField
          label="E-mail"
          type="email"
          value={email}
          onChange={(evento) => setEmail(evento.target.value)}
          required
          fullWidth
        />

        <TextField
          label="Senha"
          type="password"
          value={senha}
          onChange={(evento) => setSenha(evento.target.value)}
          required
          fullWidth
        />

        <Button type="submit" variant="contained" size="large" disabled={carregando}>
          {carregando ? <CircularProgress size={24} color="inherit" /> : 'Criar conta'}
        </Button>

        <Typography variant="body2" align="center">
          Já tem uma conta?{' '}
          <RouterLink to="/login">Entrar</RouterLink>
        </Typography>
      </Stack>
    </Box>
  )
}
