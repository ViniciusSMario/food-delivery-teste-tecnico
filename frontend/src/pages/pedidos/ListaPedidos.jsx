import Alert from '@mui/material/Alert'
import Box from '@mui/material/Box'
import Button from '@mui/material/Button'
import CircularProgress from '@mui/material/CircularProgress'
import Stack from '@mui/material/Stack'
import Typography from '@mui/material/Typography'
import { useCallback, useEffect, useState } from 'react'
import { Link as RouterLink } from 'react-router-dom'
import { atualizarStatusPedido, listarPedidos } from '../../services/pedidos'
import TabelaPedidos from './components/TabelaPedidos'

export default function ListaPedidos() {
  const [pedidos, setPedidos] = useState([])
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState(null)
  const [atualizandoId, setAtualizandoId] = useState(null)

  const carregar = useCallback(() => {
    return listarPedidos()
      .then((dados) => {
        setPedidos(dados)
        setErro(null)
      })
      .catch(() => setErro('Não foi possível carregar os pedidos.'))
      .finally(() => setCarregando(false))
  }, [])

  useEffect(() => {
    carregar()
  }, [carregar])

  async function aoAlterarStatus(id, novoStatus) {
    setAtualizandoId(id)
    setErro(null)
    try {
      const pedidoAtualizado = await atualizarStatusPedido(id, novoStatus)
      setPedidos((atual) => atual.map((p) => (p.id === id ? pedidoAtualizado : p)))
    } catch {
      setErro('Não foi possível atualizar o status do pedido.')
    } finally {
      setAtualizandoId(null)
    }
  }

  return (
    <Stack spacing={3}>
      <Stack direction="row" justifyContent="space-between" alignItems="center">
        <Typography variant="h5">Meus pedidos</Typography>
        <Button component={RouterLink} to="/pedidos/novo" variant="contained">
          Novo pedido
        </Button>
      </Stack>

      {erro && <Alert severity="error">{erro}</Alert>}

      {carregando ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', py: 6 }}>
          <CircularProgress />
        </Box>
      ) : pedidos.length === 0 ? (
        <Typography color="text.secondary">Você ainda não tem pedidos.</Typography>
      ) : (
        <TabelaPedidos
          pedidos={pedidos}
          atualizandoId={atualizandoId}
          onAlterarStatus={aoAlterarStatus}
        />
      )}
    </Stack>
  )
}
