import { Navigate, Route, Routes } from 'react-router-dom'
import LayoutPrincipal from '../layouts/LayoutPrincipal'
import LayoutPublico from '../layouts/LayoutPublico'
import EmConstrucao from '../pages/EmConstrucao'
import Login from '../pages/Login'
import Register from '../pages/Register'
import DetalhePedido from '../pages/pedidos/DetalhePedido'
import ListaPedidos from '../pages/pedidos/ListaPedidos'
import NovoPedido from '../pages/pedidos/NovoPedido'
import RotaPrivada from './RotaPrivada'
import RotaPublica from './RotaPublica'

export default function AppRoutes() {
  return (
    <Routes>
      <Route element={<RotaPublica />}>
        <Route element={<LayoutPublico />}>
          <Route path="/login" element={<Login />} />
          <Route path="/cadastro" element={<Register />} />
        </Route>
      </Route>

      <Route element={<RotaPrivada />}>
        <Route element={<LayoutPrincipal />}>
          <Route path="/pedidos" element={<ListaPedidos />} />
          <Route path="/pedidos/novo" element={<NovoPedido />} />
          <Route path="/pedidos/:id" element={<DetalhePedido />} />
        </Route>
      </Route>

      <Route path="/" element={<Navigate to="/pedidos" replace />} />
      <Route path="*" element={<EmConstrucao titulo="Pagina nao encontrada" />} />
    </Routes>
  )
}
