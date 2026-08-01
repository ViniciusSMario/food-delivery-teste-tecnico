import Alert from '@mui/material/Alert'
import Box from '@mui/material/Box'
import Button from '@mui/material/Button'
import CircularProgress from '@mui/material/CircularProgress'
import Divider from '@mui/material/Divider'
import List from '@mui/material/List'
import ListItem from '@mui/material/ListItem'
import ListItemText from '@mui/material/ListItemText'
import Paper from '@mui/material/Paper'
import Stack from '@mui/material/Stack'
import Typography from '@mui/material/Typography'
import { useCallback, useEffect, useState } from 'react'
import { Link as RouterLink, useParams } from 'react-router-dom'
import { atualizarStatusPedido, buscarPedido } from '../../services/pedidos'
import { calcularTotalPedido } from '../../utils/calcularTotalPedido'
import { formatarMoeda } from '../../utils/formatarMoeda'
import SeletorStatus from './components/SeletorStatus'

export default function DetalhePedido() {
  const { id } = useParams()

  const [pedido, setPedido] = useState(null)
  const [carregando, setCarregando] = useState(true)
  const [atualizandoStatus, setAtualizandoStatus] = useState(false)
  const [erro, setErro] = useState(null)

  const carregar = useCallback(() => {
    return buscarPedido(id)
      .then((dados) => {
        setPedido(dados)
        setErro(null)
      })
      .catch(() => setErro('Não foi possível carregar o pedido.'))
      .finally(() => setCarregando(false))
  }, [id])

  useEffect(() => {
    carregar()
  }, [carregar])

  async function aoAlterarStatus(novoStatus) {
    setAtualizandoStatus(true)
    setErro(null)
    try {
      const pedidoAtualizado = await atualizarStatusPedido(id, novoStatus)
      setPedido(pedidoAtualizado)
    } catch {
      setErro('Não foi possível atualizar o status do pedido.')
    } finally {
      setAtualizandoStatus(false)
    }
  }

  if (carregando) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', py: 6 }}>
        <CircularProgress />
      </Box>
    )
  }

  if (!pedido) {
    return <Alert severity="error">{erro ?? 'Pedido não encontrado.'}</Alert>
  }

  const total = calcularTotalPedido(pedido)

  return (
    <Stack spacing={3}>
      <Stack direction="row" justifyContent="space-between" alignItems="center">
        <Typography variant="h5">Pedido #{pedido.id}</Typography>
        <Button component={RouterLink} to="/pedidos">
          Voltar
        </Button>
      </Stack>

      {erro && <Alert severity="error">{erro}</Alert>}

      <Paper variant="outlined" sx={{ p: 2 }}>
        <Stack spacing={2}>
          <Typography variant="body1">
            <strong>Cliente:</strong> {pedido.cliente}
          </Typography>

          <Stack direction="row" justifyContent="space-between" alignItems="center">
            <Typography variant="body1">
              <strong>Endereço:</strong> {pedido.enderecoEntrega}
            </Typography>
            <SeletorStatus
              status={pedido.status}
              desabilitado={atualizandoStatus}
              onAlterar={aoAlterarStatus}
            />
          </Stack>

          <Divider />

          <List disablePadding>
            {pedido.itens.map((item) => (
              <ListItem key={item.id} disableGutters>
                <ListItemText
                  primary={item.nome}
                  secondary={`Quantidade: ${item.quantidade} × ${formatarMoeda(item.preco)}`}
                />
                <Typography variant="body2">{formatarMoeda(item.quantidade * item.preco)}</Typography>
              </ListItem>
            ))}
          </List>

          <Divider />

          <Stack direction="row" justifyContent="flex-end">
            <Typography variant="subtitle1">Total: {formatarMoeda(total)}</Typography>
          </Stack>
        </Stack>
      </Paper>
    </Stack>
  )
}
