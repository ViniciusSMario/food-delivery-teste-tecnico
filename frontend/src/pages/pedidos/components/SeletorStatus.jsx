import MenuItem from '@mui/material/MenuItem'
import Select from '@mui/material/Select'
import { STATUS_LABEL, STATUS_PEDIDO } from '../../../constants/statusPedido'

export default function SeletorStatus({ status, desabilitado, onAlterar }) {
  return (
    <Select
      size="small"
      value={status}
      disabled={desabilitado}
      onChange={(evento) => onAlterar(evento.target.value)}
    >
      {STATUS_PEDIDO.map((valor) => (
        <MenuItem key={valor} value={valor}>
          {STATUS_LABEL[valor]}
        </MenuItem>
      ))}
    </Select>
  )
}
