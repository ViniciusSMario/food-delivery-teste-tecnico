import Box from '@mui/material/Box'
import Button from '@mui/material/Button'
import Stack from '@mui/material/Stack'
import TextField from '@mui/material/TextField'
import Typography from '@mui/material/Typography'

export default function FormularioItens({ itens, onAlterarItem, onAdicionarItem, onRemoverItem }) {
  return (
    <Box>
      <Typography variant="subtitle1" gutterBottom>
        Itens do pedido
      </Typography>

      <Stack spacing={2}>
        {itens.map((item, indice) => (
          <Stack
            key={item.key}
            direction={{ xs: 'column', sm: 'row' }}
            spacing={1}
            alignItems={{ sm: 'center' }}
          >
            <TextField
              label="Item"
              value={item.nome}
              onChange={(evento) => onAlterarItem(indice, 'nome', evento.target.value)}
              required
              fullWidth
            />
            <TextField
              label="Qtd"
              type="number"
              value={item.quantidade}
              onChange={(evento) => onAlterarItem(indice, 'quantidade', evento.target.value)}
              required
              inputProps={{ min: 1 }}
              sx={{ width: { sm: 100 } }}
            />
            <TextField
              label="Preço"
              type="number"
              value={item.preco}
              onChange={(evento) => onAlterarItem(indice, 'preco', evento.target.value)}
              required
              inputProps={{ min: 0, step: '0.01' }}
              sx={{ width: { sm: 120 } }}
            />
            <Button
              color="error"
              onClick={() => onRemoverItem(indice)}
              disabled={itens.length === 1}
            >
              Remover
            </Button>
          </Stack>
        ))}
      </Stack>

      <Button onClick={onAdicionarItem} sx={{ mt: 2 }}>
        + Adicionar item
      </Button>
    </Box>
  )
}
