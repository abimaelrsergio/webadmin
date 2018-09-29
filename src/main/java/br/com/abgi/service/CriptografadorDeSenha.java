package br.com.abgi.service;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ejb.Stateless;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * @author abimael
 * @version 1
 * 
 */
@Stateless
public class CriptografadorDeSenha implements Serializable {

	private static final String ALGORITHM = "SHA1";
	private static final long serialVersionUID = 1L;

	public String criptografar(String senha) {

		try {

			MessageDigest md = MessageDigest.getInstance(ALGORITHM);
			md.update(senha.getBytes());
			senha = new String(Base64.encodeBase64(md.digest()));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return senha;
	}

}
