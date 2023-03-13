package gob.regionancash.atencionciudadano.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.io.File;
import java.io.FileReader;
import java.security.Key;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("${mp.jwt.verify.publickey.location}")
  private String publicKeyFile;

  //private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

  public String getUsername(String token) {
    final Map claims = (Map)getClaims(token);
    System.out.println(claims);
    return (String)claims.get("user");
    //return getClaim(token, Claims::getSubject);
  }

  public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = (Claims)getClaims(token);
    return claimsResolver.apply(claims);
  }

  public Object getClaim(String token,String field) {
    final Map claims = (Claims)getClaims(token);
    return claims.get(field);//claimsResolver.apply(claims);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = getUsername(token);
    System.out.println(userDetails.getUsername());
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return getExpiration(token).before(new Date());
  }

  private Date getExpiration(String token) {

    return (Date)((Claims)getClaims(token)).getExpiration();//.get(Claims.EXPIRATION);//getClaim(token, Claims::getExpiration);
  }

  public Map getClaims(String token) {
    SignatureAlgorithm sa = SignatureAlgorithm.HS256;
    try {
      Key rsa=readPublicKey(new File(publicKeyFile));
      return (Map)Jwts
          .parserBuilder()
          .setSigningKey(rsa)
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
/*
  private Key getSignInKey(String secretKey) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey != null ? secretKey : SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
*/
  public static RSAPublicKey readPublicKey(File file) throws Exception {
    try (FileReader keyReader = new FileReader(file)) {
      PEMParser pemParser = new PEMParser(keyReader);
      JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
      SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(pemParser.readObject());
      return (RSAPublicKey) converter.getPublicKey(publicKeyInfo);
  }
  }

}
