
import java.math.BigInteger;

object AccountFamily {
  import BigInteger.{ZERO, ONE}
  import org.bouncycastle.math.ec.{ECPoint, ECCurve}
  import org.bouncycastle.util.encoders.Hex
  import org.bouncycastle.asn1.sec.SECNamedCurves
  import org.bouncycastle.crypto.digests.SHA512Digest
  //import org.bouncycastle.math.ec.ECAlgorithms
  //import org.bouncycastle.crypto.params.ECKeyParameters;
  //import org.bouncycastle.jce.ECKeyUtil;
  //import org.bouncycastle.jce.spec.ECNamedCurveSpec

  val ecparams = SECNamedCurves.getByName("secp256k1")
  val paramN = ecparams.getN()


  private[this] def SHA512Half(input:Array[Byte]) = {
	val dgst = new SHA512Digest
	val result512 = new Array[Byte](64)
	dgst.update(input, 0, input.length)
	dgst.doFinal(result512, 0)
	new BigInteger(result512.take(32))
  }
  private[this] def ECpriv2pub(priv:BigInteger) = {
	val pub = ecparams.getG().multiply(priv)
	val cpub = new ECPoint.Fp(ecparams.getCurve, pub.getX, pub.getY, true)
	cpub.getEncoded
  }
  private[this] def padBN(bn: BigInteger, len:Int) = {
	bn.toByteArray().toList.reverse.padTo(len, 0:Byte).reverse
  }
  private[this] def padCons(first: Seq[Byte], conseq: Int) = {
	first ++ BigInteger.valueOf(conseq).toByteArray.toList.reverse.padTo(4, 0:Byte).reverse
  }
  private[this] def Base58en(bn: BigInteger) = {
	val syms = "rpshnaf39wBUDNEGHJKLM4PQRST7VWXYZ2bcdeCg65jkm8oFqi1tuvAxyz"
	val base = BigInteger.valueOf(syms.length)
	var nbn = bn
	do {
	  nbn = nbn.divide(base)
	  val s = syms.charAt(nbn.mod(base).intValue)
	  print(s)
	}while (nbn.compareTo(ZERO) > 0)
	println()
  }
  def TestVectors() {
	{
	  val rightret = new BigInteger(Hex.decode("B8244D028981D693AF7B456AF8EFA4CAD63D282E19FF14942C246E50D9351D22"))
	  assert(SHA512Half(Array[Byte](0)).compareTo(rightret) == 0)
	}
	{
	  val rightret = new BigInteger(Hex.decode("8EEE2EA9E7F93AB0D9E66EE4CE696D6824922167784EC7F340B3567377B1CE64"))
	  val arr:Array[Byte] = padCons(Seq.empty, 100000).toArray
	  assert(SHA512Half(arr).compareTo(rightret) == 0)
	}
	{
	  val rightret = new BigInteger(Hex.decode("7CFBA64F771E93E817E15039215430B53F7401C34931D111EAB3510B22DBB0D8"))
	  val seed = new BigInteger(Hex.decode("71ED064155FFADFA38782C5E0158CB26"))
	  assert(GenerateRootDeterministicKey(seed).compareTo(rightret) == 0)
	}
  }
  def test() {
	TestVectors()
	//println(() map(c => "%02x".format(c)) mkString(" ")))
  }
  def GenerateRootDeterministicKey(seed: BigInteger) = {
	val dgst = new SHA512Digest
	val result512 = new Array[Byte](64)
	var seq = 0
	var priv256 = seed
	do {
	  val input160 = padCons(padBN(seed, 16), seq).toArray
	  priv256 = SHA512Half(input160)
	  seq = seq + 1
	}
	while (priv256.equals(ZERO)  || (priv256.compareTo(paramN) >= 0))

	Base58en(new BigInteger(ECpriv2pub(priv256)))
	
	priv256
  }
  def GeneratePublicDeterministicKey() {
  }
  def GeneratePrivateDeterministicKey() {
  }
}

object MainApp extends App
{
  //val kp = new ECPrivateKeyParameters()
  //val gps:ECGenParameterSpec = new ECGenParameterSpec ("secp256r1");
  AccountFamily.test()
}




