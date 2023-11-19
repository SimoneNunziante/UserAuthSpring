package it.corso.helper;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Base64;

public class SecurityManager
{
	public static String encode(String toEncode)
	{
		return Base64.getEncoder().encodeToString(toEncode.getBytes());
	}
	
	public static String decode(String toDecode)
	{
		return new String(Base64.getDecoder().decode(toDecode));
	}
	
	public static String generateToken(String mail)
	{
		LocalDateTime now = LocalDateTime.now();
		Instant instant =now.toInstant(OffsetDateTime.now().getOffset());
		long timestamp = instant.getEpochSecond() * 1000;
		return encode(mail) + "_" + timestamp;
	}
}