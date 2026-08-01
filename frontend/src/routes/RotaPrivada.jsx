import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { estaAutenticado } from '../services/token'

/**
 * Guarda das rotas autenticadas. Sem token, redireciona para o login e guarda
 * a rota de origem, para poder voltar a ela depois de autenticar.
 */
export default function RotaPrivada() {
  const location = useLocation()

  if (!estaAutenticado()) {
    return <Navigate to="/login" replace state={{ de: location.pathname }} />
  }

  return <Outlet />
}
