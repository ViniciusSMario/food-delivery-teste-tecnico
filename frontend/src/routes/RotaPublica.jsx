import { Navigate, Outlet } from 'react-router-dom'
import { estaAutenticado } from '../services/token'

/**
 * Guarda das rotas publicas (login/cadastro). Com token valido, redireciona
 * para a area logada em vez de deixar reautenticar.
 */
export default function RotaPublica() {
  if (estaAutenticado()) {
    return <Navigate to="/pedidos" replace />
  }

  return <Outlet />
}
