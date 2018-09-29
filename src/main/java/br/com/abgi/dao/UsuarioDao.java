package br.com.abgi.dao;

import java.io.Serializable;

import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import br.com.abgi.model.UsuarioEntity;

/**
 * 
 * @author abimael
 * @version 1
 * 
 */
@Named
//@Log4j
public class UsuarioDao extends GenericDAO<UsuarioEntity>  implements Serializable {

	private static final long serialVersionUID = 1L;

	public UsuarioEntity fingByLogin(final String login) {
		UsuarioEntity usuario = null;
		try {
			final TypedQuery<UsuarioEntity> query = this.getEm().createNamedQuery("UsuarioEntity.findByLogin", UsuarioEntity.class);
			query.setParameter("login", login);
			usuario = query.getSingleResult();
		} catch (NoResultException n) {
//			log.error("Usuario não encontrado: " + login);
		} catch (NonUniqueResultException nu) {
//			log.error("Multiplos usuarios foram identificados: " + login);
		}
		return usuario;
	}

	public UsuarioEntity findByLoginSenha(String login, String senha) {

		TypedQuery<UsuarioEntity> query = this.getEm().createNamedQuery("UsuarioEntity.findByLoginSenha", UsuarioEntity.class);
		query.setParameter("login", login);
		query.setParameter("senha", senha);
		UsuarioEntity usuarioEntity = null;
		try {
			usuarioEntity = query.getSingleResult();
		} catch (NoResultException n) {
//			log.error("Usuario não encontrado: " + login);
		} catch (NonUniqueResultException nu) {
//			log.error("Multiplos usuarios foram identificados: " + login);
		}
		return usuarioEntity;
	}

}
