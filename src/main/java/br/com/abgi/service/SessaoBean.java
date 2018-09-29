package br.com.abgi.service;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.abgi.dao.UsuarioDao;
import br.com.abgi.model.UsuarioEntity;

/**
 * 
 * @author abimael
 * @version 1
 *
 */
@Stateless
public class SessaoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	@EJB
	private CriptografadorDeSenha criptografadorBean;
	@Inject
	private UsuarioDao dao;


	public UsuarioEntity findByLoginSenha(final String login, final String senha) {
		final String criptografada = criptografadorBean.criptografar(senha);
		return dao.findByLoginSenha(login, criptografada);
	}

	public UsuarioEntity findUsuariosByLogin(final String login) {		
		return dao.fingByLogin(login);
	}

//	public static void main(String[] args) {
//		
//		String senha = "4estGump";
//		
//		CriptografadorDeSenha criptografadorBean = new CriptografadorDeSenha();
//		System.out.println(criptografadorBean.criptografar(senha));
//	}
}
