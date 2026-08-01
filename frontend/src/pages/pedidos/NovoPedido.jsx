import Alert from '@mui/material/Alert'
import Box from '@mui/material/Box'
import Button from '@mui/material/Button'
import CircularProgress from '@mui/material/CircularProgress'
import Stack from '@mui/material/Stack'
import TextField from '@mui/material/TextField'
import Typography from '@mui/material/Typography'
import { useState } from 'react'
import { Link as RouterLink, useNavigate } from 'react-router-dom'
import { criarPedido } from '../../services/pedidos'
import FormularioItens from './components/FormularioItens'

function itemVazio() {
  return { key: crypto.randomUUID(), nome: '', quantidade: 1, preco: '' }
}

export default function NovoPedido() {
  const navigate = useNavigate()

  const [cliente, setCliente] = useState('')
  const [enderecoEntrega, setEnderecoEntrega] = useState('')
  const [itens, setItens] = useState([itemVazio()])
  const [enviando, setEnviando] = useState(false)
  const [erro, setErro] = useState(null)

  function aoAlterarItem(indice, campo, valor) {
    setItens((atual) => atual.map((item, i) => (i === indice ? { ...item, [campo]: valor } : item)))
  }

  function adicionarItem() {
    setItens((atual) => [...atual, itemVazio()])
  }

  function removerItem(indice) {
    setItens((atual) => atual.filter((_, i) => i !== indice))
  }

  async function aoSubmeter(evento) {
    evento.preventDefault()
    setErro(null)
    setEnviando(true)

    try {
      const payload = {
        cliente,
        enderecoEntrega,
        itens: itens.map((item) => ({
          nome: item.nome,
          quantidade: Number(item.quantidade),
          preco: Number(item.preco),
        })),
      }
      await criarPedido(payload)
      navigate('/pedidos', { replace: true })
    } catch (excecao) {
      const mensagem = excecao.response?.data?.mensagem ?? 'Não foi possível criar o pedido.'
      setErro(mensagem)
    } finally {
      setEnviando(false)
    }
  }

  return (
    <Box component="form" onSubmit={aoSubmeter} noValidate>
      <Stack spacing={3}>
        <Typography variant="h5">Novo pedido</Typography>

        {erro && <Alert severity="error">{erro}</Alert>}

        <TextField
          label="Cliente"
          value={cliente}
          onChange={(evento) => setCliente(evento.target.value)}
          required
          autoFocus
          fullWidth
        />

        <TextField
          label="Endereço de entrega"
          value={enderecoEntrega}
          onChange={(evento) => setEnderecoEntrega(evento.target.value)}
          required
          fullWidth
        />

        <FormularioItens
          itens={itens}
          onAlterarItem={aoAlterarItem}
          onAdicionarItem={adicionarItem}
          onRemoverItem={removerItem}
        />

        <Stack direction="row" spacing={2}>
          <Button type="submit" variant="contained" disabled={enviando}>
            {enviando ? <CircularProgress size={24} color="inherit" /> : 'Criar pedido'}
          </Button>
          <Button component={RouterLink} to="/pedidos" disabled={enviando}>
            Cancelar
          </Button>
        </Stack>
      </Stack>
    </Box>
  )
}
