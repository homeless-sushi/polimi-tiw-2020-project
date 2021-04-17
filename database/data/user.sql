-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: localhost    Database: tiw-project
-- ------------------------------------------------------
-- Server version	8.0.23

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (00000001,'Giovanni.D\'Angelo@mail.polimi.it','$2a$12$crck4UrbfLfrx3aSgbr0TO64CRLV/sydF4xfXYX5yDR4G8wYCGnDq','Giovanni','D\'Angelo'),(00000002,'Matilde.Ferri@mail.polimi.it','$2a$12$JBYqTR5wb1CzGCyKfnrH4.7uqOoOr2jr.WF7cMEWkJEDW2ialX/aq','Matilde','Ferri'),(00000003,'Diego.Fontana@mail.polimi.it','$2a$12$fPukbNAQV8.TcQHS2paTbOITIUcMJ3Nt4CZ8qGPyb9pnGx/PFguo.','Diego','Fontana'),(00000004,'Giulia.Esposito@mail.polimi.it','$2a$12$gUJloBWzIpuYVvbNUv46mu4LMaVxavsSHPYtjI8B1Z8bjcn6ElK2O','Giulia','Esposito'),(00000005,'Erica.Poletti@mail.polimi.it','$2a$12$EWYL.93qXF0Qls.aIyYjlOn/yO/1Sx3N4WKf2mE2L81hwmKqFegVe','Erica','Poletti'),(00000006,'Giovanni.Zanella@mail.polimi.it','$2a$12$as5qo8I5J/dhrddry3l6X.Aa9e7G1KU876paI8C9l.mcEWqNGiz9.','Giovanni','Zanella'),(00000007,'Enrico.Sala@mail.polimi.it','$2a$12$SJua6xhBA/RRYYo2w14kIug/AHHkra8TzdivCbKkANIYGLaeRgJMq','Enrico','Sala'),(00000008,'Maria.Martini@mail.polimi.it','$2a$12$JQWFWHz6/DqrlBZ5y2JOiu33dQ1ykAkKTmZp2czEm3zeYGexao66K','Maria','Martini'),(00000009,'Filippo.Gatti@mail.polimi.it','$2a$12$QvVGpEXk7ZOGIKb/CL.5OOI4/hT1EP6WiFcZb3nDCpcz.37HNfu46','Filippo','Gatti'),(00000010,'Giovanni.Gallo@mail.polimi.it','$2a$12$rgX0iR1FysyMLzxcQCQvSe09tjt44FS3boeuvAXeK1hp30asVZjSO','Giovanni','Gallo'),(00000011,'Alessandro.Martini@mail.polimi.it','$2a$12$J9H7Wh8HfiiA35Qxm6NDTeaUJ8NenNFSDkD8mWKG2EyPIWWnKp0r6','Alessandro','Martini'),(00000012,'Elisa.Ferri@mail.polimi.it','$2a$12$l0JgHKIUvNTRGiYdN0X89.oUINmkCf1D7hWcK33deuINAnQpVDf82','Elisa','Ferri'),(00000013,'Giulia.Mariani@mail.polimi.it','$2a$12$iE.9sAMCrTpSXE4mgxEzle0bDXwVSvzsrqrIT/2kk.IFl9oLvlPhG','Giulia','Mariani'),(00000014,'Martina.Gallo@mail.polimi.it','$2a$12$Koex9xKB5ZiZzN1uONWLkeGW6tiJu23fy3cCK8lt2tMTtYLIwfsiK','Martina','Gallo'),(00000015,'Matteo.Martini@mail.polimi.it','$2a$12$.dtLfT2HjXckgrV86Sr4Pe0qZJmxLSPmp98wBl.cXWiErygsxzJ1.','Matteo','Martini'),(00000016,'Greta.Poletti@mail.polimi.it','$2a$12$Td/wLlkJ6.br9fnqA7x1t.q.e/MmRXAFrGrr3w32O0HdQhtFqlalq','Greta','Poletti'),(00000017,'Daniele.Greco@mail.polimi.it','$2a$12$70kvl5PJHwrpDq5nsMYh5OffqSjc.MBPQ/rfLg/YEJugaHOETbXr6','Daniele','Greco'),(00000018,'Tommaso.Marini@mail.polimi.it','$2a$12$.whoo4auux.6Ias4GcLi8uhkw2K5bC1HvkVCpY4SJspiFOTxDEuVq','Tommaso','Marini'),(00000019,'Anna.Fabbri@mail.polimi.it','$2a$12$UkYh8mmnTwU1j41LPkz9su7SEFVbPK20VnNSNe64abksKLx4iVdYi','Anna','Fabbri'),(00000020,'Enrico.Ferri@mail.polimi.it','$2a$12$.Hp1fOCDTeO79TJ5jbjISeQaOiqqHKpe7qLMtS.DNju6zlkP7YgT2','Enrico','Ferri'),(00000021,'Emma.Esposito@mail.polimi.it','$2a$12$Rsv6mIqB6ayCXhxipQkP0eReIsxkPiiHkk8IGj/fBmaBDUJd6cN.K','Emma','Esposito'),(00000022,'Ginevra.Serra@mail.polimi.it','$2a$12$6j.66BqkfdcAKWJVeKCCUe.oYjbJNrtmuHkCg8niStH.YVAcNZl7O','Ginevra','Serra'),(00000023,'Noemi.Ferrari@mail.polimi.it','$2a$12$De1Z7kvZvYcF3/NDrUopjOsV2WnVHzVQrmgOIm1vCQPoYpeXHqBBy','Noemi','Ferrari'),(00000024,'Francesca.Bernardi@mail.polimi.it','$2a$12$eBtcm6vLYx3pAc7ZOXDNvOAruXckg6F4bD0elFRmoRNbb3eoetwZe','Francesca','Bernardi'),(00000025,'Aurora.Pace@mail.polimi.it','$2a$12$fvU3qSfOlqqOFIuXth0Tv.HIxh77EwmW54H/9N.ND.nBHKQQiGhaG','Aurora','Pace'),(00000026,'Anna.Sartori@mail.polimi.it','$2a$12$SfbRUtTy2W/hP2lHlhHWQevd0K/5KeqZuPN8qnT50NJE.Q6V9VRd.','Anna','Sartori'),(00000027,'Elisabetta.Moretti@mail.polimi.it','$2a$12$lOMY7rmx4nUNoscX/ppXEeq/WrpdxS5HzY97qdvkuKf85sK1Ms54G','Elisabetta','Moretti'),(00000028,'Noemi.Rossetti@mail.polimi.it','$2a$12$CRvBc0ovPp4AQMZP6Ao4A.msda7w4VkjB0AMsWNOAIIoEWzqO5Kjm','Noemi','Rossetti'),(00000029,'Samuele.Rossetti@mail.polimi.it','$2a$12$2kVNStYN0B8/Iuom6epXQ.JbPMsOlQ5R3.1HmXMJ6rpCGnqTJBgN2','Samuele','Rossetti'),(00000030,'Diego.Pellegrini@mail.polimi.it','$2a$12$oFcEsxT.dyCV6wDNpaXrqupXJrpA0cY34qiAUUn89u5KGfe3iY2bC','Diego','Pellegrini'),(00000031,'Elisabetta.Longo@mail.polimi.it','$2a$12$K6k9iZERV7JyVwsRi6Kdbenbga9KvXroMEFHWKUOW1msradU/Ra7y','Elisabetta','Longo'),(00000032,'Federico.Rizzi@mail.polimi.it','$2a$12$xCaf77T/bjFaWA83vhEMP.GIvg/ytYi7KnU5fu97vsQ3ILj2WYjGO','Federico','Rizzi'),(00000033,'Alice.Greco@mail.polimi.it','$2a$12$nf7bQTg0dwSbuRbM07XYN.ezTshQnEvVRwp.hi0yMmC9R2wDf4zzC','Alice','Greco'),(00000034,'Riccardo.Parodi@mail.polimi.it','$2a$12$p41/ouLVWibYjHQQgFsZ/OdczBYP5XlZIdU4x7IRMMBM2ErGwQm1i','Riccardo','Parodi'),(00000035,'Sara.Longo@mail.polimi.it','$2a$12$2JLnIjGH.3Q0e6dkYIlQj.12hilibPv3qtAtvM0ZiGQvJlSjwrAtG','Sara','Longo'),(00000036,'Daniele.Pacini@mail.polimi.it','$2a$12$6HgTovHEZb4oCnJscX1TCObOVxdwyD2HzU5VeljzSdj7CKd6gZGQC','Daniele','Pacini'),(00000037,'Chiara.Gori@mail.polimi.it','$2a$12$CjjQvx1TDKYDchEVGZgrKeuz9ig75fGFjmZyX5WCaVuE38Y5ZL8AK','Chiara','Gori'),(00000038,'Francesca.Giordano@mail.polimi.it','$2a$12$aHYC8XHb.R0nt.EHb.1uO.2Xy.zaik8.8AHKtiml5ltdRs.IRgK.u','Francesca','Giordano'),(00000039,'Leonardo.Marchetti@mail.polimi.it','$2a$12$5rNft0chb15bpwz3W4IKS.iYR7fW.uTtcmqtZH5hXU/1PP0n53pr6','Leonardo','Marchetti'),(00000040,'Giulia.Ferrara@mail.polimi.it','$2a$12$FPC3y8l1T5/q8ptHQM4Iv.vzGYs5Q10vBf1Eu3T6GfKHV1NuMWeTC','Giulia','Ferrara'),(00000041,'Leonardo.Leone@mail.polimi.it','$2a$12$uJXBZKBEpzHbWrx9.7elIedYXl9WUXeoNriaA8DzIPqt3VRH3nmUW','Leonardo','Leone'),(00000042,'Anna.Farina@mail.polimi.it','$2a$12$SLG58hRtjBlbGiXykYQxyu6/6py866K7ObhPhavjXEcNRkp4Zqmti','Anna','Farina'),(00000043,'Lorenzo.Marino@mail.polimi.it','$2a$12$O1dQ0a5Tk/gi7YSm1XPDYuAeL6yoaKEIhwhOV7ooZ8d02EtpCjSKC','Lorenzo','Marino'),(00000044,'Elisa.Gallo@mail.polimi.it','$2a$12$v7AhG0EEOJ3RRhU8q1QDQuXtEejAu/pL9CCDRBmcadetQryZHtv32','Elisa','Gallo'),(00000045,'Marco.Giordano@mail.polimi.it','$2a$12$44s17MB4/2HNsLCCy4bp1.plWvkqXdZO1H4R1WiYpSO3w7/qUnZrC','Marco','Giordano'),(00000046,'Giovanni.Poletti@mail.polimi.it','$2a$12$7nO74K1tQSVyH/nzkXxB9e6.XzRJltRLjZbEJ/g46NFOcsTc.bmMG','Giovanni','Poletti'),(00000047,'Aurora.Romano@mail.polimi.it','$2a$12$3g7P4VZPn3zKtSporFAmBuEXsRXnsSkA0UyH7u2FEFNwXxiDcVYiS','Aurora','Romano'),(00000048,'Samuele.Marchetti@mail.polimi.it','$2a$12$gtFjc0Za6UcBmp7SLnKhP.7zl0KMYcvC/uCjwiLnwwiUBcU3t/hLO','Samuele','Marchetti'),(00000049,'Lorenzo.Bisio@mail.polimi.it','$2a$12$xry4JaT5smT/Q4ixIOX49en1UR4Gv4OwUOzCSjQM1NI9XsrXMhyve','Lorenzo','Bisio'),(00000050,'Simone.Ricci@mail.polimi.it','$2a$12$9qja9DnceyJZ5PFCSjGuAuFV98RLl6mG/e86ewdmJJXZniBDXltpe','Simone','Ricci'),(00000051,'Davide.Leone@mail.polimi.it','$2a$12$KO.8wrDDwoLKwW5Q43IyxO9DiRbCru368Ux0YbrZ8pz4th13f6dv6','Davide','Leone'),(00000052,'Beatrice.Marini@mail.polimi.it','$2a$12$Rt9BG5TGtVbYvPpHAO0FqurLLoUS7bR0Ujlel/sGYmQ.4Pgd1IO9C','Beatrice','Marini'),(00000053,'Caterina.Ferrari@mail.polimi.it','$2a$12$abiycl4zntW1NsafPkoxnumVViddgp9C8KOri.Op2QOrMqqJD2hoa','Caterina','Ferrari'),(00000054,'Luca.Gallo@mail.polimi.it','$2a$12$o.x24ODH5UrEQD89zqmpXuXlMqtm1/3XIDpFesPvLw2RGBQq.UjTq','Luca','Gallo'),(00000055,'Ginevra.Marini@mail.polimi.it','$2a$12$96k2QwvdtH22w3n3KijOyOABbG4SDA1bbgZuZB.OjEXhM4eBld3zu','Ginevra','Marini'),(00000056,'Antonio.Gori@mail.polimi.it','$2a$12$nDHSJacU7UhVtwWs4iohM.nIvTElSSGDE2JG1gTIj5qGeo0g9tTfC','Antonio','Gori'),(00000057,'Alessandro.Serra@mail.polimi.it','$2a$12$3MWyqL1qGIE.efSAeOWs0e/lo519361j1Na0VoRkrHC.wP5XYvdH.','Alessandro','Serra'),(00000058,'Matteo.Pellegrini@mail.polimi.it','$2a$12$lhbaNCQTgsK5FYifsULSc.hsL6I7tfF6tGtDRW3cS5HTL3AuYDnGC','Matteo','Pellegrini'),(00000059,'Diego.Ricci@mail.polimi.it','$2a$12$jmKjJAzJBS.D9f8E4OlAauk8oCti4eo2SuVv7n3iv6pMXoVOG5jOC','Diego','Ricci'),(00000060,'Elisa.Serra@mail.polimi.it','$2a$12$66FeP7ZImeVZE0kg6.Deiedwaf.A4dutzkMDd3/mOOwovEvjwAA/O','Elisa','Serra'),(00000061,'Ginevra.Melis@mail.polimi.it','$2a$12$VyM/q3fNJ79NBAT0wdgMguVYFFBzXtq7GpkYpPG5bgtSUN41ITLRy','Ginevra','Melis'),(00000062,'Diego.Bernardi@mail.polimi.it','$2a$12$JpRzgns1N1eVE0R3m.vrtujdOQS.clX70cHhAxwP9dVSPXWHtvORe','Diego','Bernardi'),(00000063,'Matilde.Serra@mail.polimi.it','$2a$12$JWjWIOAzVT7NSCeH5jS/S.Mh4Y9bZnTiYvptTDl8aO2iCmfwZ1jNi','Matilde','Serra'),(00000064,'Maria.Mariani@mail.polimi.it','$2a$12$zf8lFfNBPp2lWzsEBLt5g.vO..eUa/q6ehxmEmBAUnU/5SVX.09Ge','Maria','Mariani'),(00000065,'Antonio.Marchetti@mail.polimi.it','$2a$12$VI3LmwrJlRo5L8Xub12RXePgyHNg6xL1FF62syGebsqNJC1agoUxS','Antonio','Marchetti'),(00000066,'Matilde.Grassi@mail.polimi.it','$2a$12$ONObVcJdPvMTTTd.I9c80eJs7LL9Ym.BMfc9mEO4kODKrF0vG8aA2','Matilde','Grassi'),(00000067,'Elisa.Milani@mail.polimi.it','$2a$12$m4aWAG1Hmjm06reVtRBirePxO7BUDUKeX3IGJEwxwq9TzInmMhDXq','Elisa','Milani'),(00000068,'Giovanni.Costa@mail.polimi.it','$2a$12$z1iqvGetb2.bCOh4cNydt.HDGfYyBpf999NX99BKFVqZm6eqHmsri','Giovanni','Costa'),(00000069,'Elisabetta.Marino@mail.polimi.it','$2a$12$Ir4YDfRDDMNkLgZumACm5.WgiIBXP3MKynAsEQpXWavPwYOqkf6w6','Elisabetta','Marino'),(00000070,'Sofia.Marini@mail.polimi.it','$2a$12$erjR3eoWGYCB4rVbd0wCaeeszfWam88Rlx0Msmjiqh.55xzGGRuBi','Sofia','Marini'),(00000071,'Sofia.Melis@mail.polimi.it','$2a$12$QSH.AwJBSEo8rz5G1ErFBeAjsG7UlzxZulx1KRRB2xLwzKmw2b59O','Sofia','Melis'),(00000072,'Caterina.Barbieri@mail.polimi.it','$2a$12$gW8Xi0DewdmTs9XFAIbwE.U6jAstVOw1PaEzAUb8Ejdxv52.ICWnK','Caterina','Barbieri'),(00000073,'Edoardo.D\'Angelo@mail.polimi.it','$2a$12$04J7x7gYCC8fbRV3NGs8Xu7mn1ACA9JowskaNhl.MK78/iqBHDndm','Edoardo','D\'Angelo'),(00000074,'Greta.Lombardi@mail.polimi.it','$2a$12$aLEK15GAPbBXu8S.aKuU1eZwJrDdy.xZucUEYp923Y/KgI4e66372','Greta','Lombardi'),(00000075,'Giorgio.Marino@mail.polimi.it','$2a$12$xRmYLnxAmO1ATAOqz14M/eg2b1CaYUYoQsjYi25tpm5CssdX6htKy','Giorgio','Marino'),(00000076,'Lorenzo.Galli@mail.polimi.it','$2a$12$f3KOoZhNo0PlyzwWOqegsugqwjiTGlrTmnO8xzeBGSHcrrVrMliOC','Lorenzo','Galli'),(00000077,'Alessia.Barbieri@mail.polimi.it','$2a$12$7BSmYaY1RXPgLuUrzTY8eOOwp4bwN6mVxsa2LVMNDHD9bVlCFgZqa','Alessia','Barbieri'),(00000078,'Giada.Bianchi@mail.polimi.it','$2a$12$.s7yGST4UMN3g/vR73kuMOpzNhdqQWjE7gKdJCeU3rOWTy.cAEqeW','Giada','Bianchi'),(00000079,'Sara.Ferri@mail.polimi.it','$2a$12$tRL8.51cP/Ys8ED1YDXBFO6ipXHIfAMFE1b3zDG7/SN24lhkxYS0G','Sara','Ferri'),(00000080,'Greta.Morelli@mail.polimi.it','$2a$12$MT0pwd39kY3R1Dd2pF2qcugMG8vPbWkyLWt8/t3Y2AT3YSTYsQDrO','Greta','Morelli'),(00000081,'Maria.Colombo@mail.polimi.it','$2a$12$dydQNtWFkXtCm9hfr.l9uOrHJ.Ovm7OeZM66cS8GrDADSKZOOOqji','Maria','Colombo'),(00000082,'Matilde.D\'Angelo@mail.polimi.it','$2a$12$H6nVcs2reZDHU6ywzLHIxuuum1LBXyxlbX3wK//GMSEm93EcLFb1S','Matilde','D\'Angelo'),(00000083,'Francesca.Sanna@mail.polimi.it','$2a$12$rHTtpkKxpHwpXFFHBir4ReZFxFtP1wPliRrBrHLAoVinBhRFqOEYy','Francesca','Sanna'),(00000084,'Diego.Ferraro@mail.polimi.it','$2a$12$cmz3/ox4TaMhz5HkyrjFY.muywoN7iNWvqbBU7g3PPzzAz6zseJz6','Diego','Ferraro'),(00000085,'Gaia.Martinelli@mail.polimi.it','$2a$12$gs63AI1xSJFy2P2lBVNfKOzm7ja3flQs2TkslHYIMhFIxUK9iDxOy','Gaia','Martinelli'),(00000086,'Enrico.Poletti@mail.polimi.it','$2a$12$u9FmFqssN4EAljEqyX3aW.v7dzF7ChrljyYb4ox13VaRzWa/Li8PS','Enrico','Poletti'),(00000087,'Diego.Mancini@mail.polimi.it','$2a$12$S.cYl1wP6r.S4vxUqgBOhO7IGfSi4YqOcK7COT.wGLDMAiCckQoji','Diego','Mancini'),(00000088,'Diego.Esposito@mail.polimi.it','$2a$12$sMjGYKrNeZT8rJhh98QU1uouZ4kSF48pKxJZZF7bW5S3QusJJce6C','Diego','Esposito'),(00000089,'Giorgia.Bruno@mail.polimi.it','$2a$12$bN20muNDvepVfABTVx4Il.5hJ0CoeNcLZiLtDQYxmuZ9gdJYJ2Azi','Giorgia','Bruno'),(00000090,'Alessandro.Morelli@mail.polimi.it','$2a$12$k7too7Jirjci8Tbz3zvSmOBYUwbNHrTEgclb2WUWyc1ViAByoXyA.','Alessandro','Morelli'),(00000091,'Diego.Pacini@mail.polimi.it','$2a$12$vGJA2sH2iIsX3EaQ7OP7VuJHNRzSxJuIInk/GzDOz38tg.Urmu/NK','Diego','Pacini'),(00000092,'Giorgio.Russo@mail.polimi.it','$2a$12$S5LH9.LAqTJm3hVEt6MeQuoG0fL2tW3KRm4tz1wargU0oX6T92Y6.','Giorgio','Russo'),(00000093,'Sara.Ferrari@mail.polimi.it','$2a$12$gC8lyQNPOXR4olxwL8yjJeVVvAA77pYutxaZ3gZcSzwIW1p5cxdq.','Sara','Ferrari'),(00000094,'Simone.Mariani@mail.polimi.it','$2a$12$fgNMvzGD4ySeBFWUoRskROxlRUXDMFSo5Yfcjp9EHtIzsK/PBKbnK','Simone','Mariani'),(00000095,'Giada.Santoro@mail.polimi.it','$2a$12$jj1o6uy6TOHMk2bBsPNY9.X5AJs6bo.2O0LdyNnRUHalMjfCB6sO6','Giada','Santoro'),(00000096,'Daniele.Pace@mail.polimi.it','$2a$12$y.089hwLfwkk1zmZEu34.eyuYKNv6rJA1yUf1uazo22ZiF1qIQCLS','Daniele','Pace'),(00000097,'Gaia.Gori@mail.polimi.it','$2a$12$kcIfBnhNxqhxn4BYFciq6OP2S8qNmu6zqLxvZiicTlevBqOGCZQU6','Gaia','Gori'),(00000098,'Daniele.Poletti@mail.polimi.it','$2a$12$LFeUU11mijWWelaXfzD4heRZWqvbSUvf71jy1fZqWNraPSNxZRNSm','Daniele','Poletti'),(00000099,'Vittoria.Ferrara@mail.polimi.it','$2a$12$SYcraf.3D1lag71WWeIJ9e9vYNNu00CZvCn9AHrNhZSjIu0Q8LhpG','Vittoria','Ferrara'),(00000100,'Alessia.Russo@mail.polimi.it','$2a$12$sPJQG0W8uAUB4IsQFrwJg.44hHcfmqu73m2jYdbpvMT3rOjeIn12O','Alessia','Russo'),(00000101,'Davide.Pacini@mail.polimi.it','$2a$12$jlZyggtUlHWMWgT4.FcvC.e8nHTmO16G/bkfDsarm3qmbjygSxbPG','Davide','Pacini'),(00000102,'Marco.Conte@mail.polimi.it','$2a$12$GJ3MAaresqgeNl1shT4Pnu/CQE7ZnnxydRkxRr/B1K/DpHORZBef.','Marco','Conte'),(00000103,'Greta.Gatti@mail.polimi.it','$2a$12$6BBMDtLrTypDM1WZ.I7HfOTzOPqsNRCuPn1/g5HYaCFSSHQ7GiOVi','Greta','Gatti'),(00000104,'Ginevra.Conti@mail.polimi.it','$2a$12$60luB9SVuSkoTf9.XbpkuOcO8yJniqDd/QWU05LLFHPdQoBtaQdPC','Ginevra','Conti'),(00000105,'Samuele.Parodi@mail.polimi.it','$2a$12$/eNKsYZebdpphQT8WUqnBOCv4/5Dl4txev3yVXpi2fcrsf9MG6Iem','Samuele','Parodi'),(00000106,'Margherita.Conti@mail.polimi.it','$2a$12$BdCCK9Zpi4SpsWTAXiJhqeGtcyDZR2C16EzSMgQsB5Xwj69bBmaK.','Margherita','Conti'),(00000107,'Filippo.Ferrara@mail.polimi.it','$2a$12$ltrcGM5hPIyw2N4.bCh/fu46od2NPPs.MzJepRiA3bAl.lqeia55S','Filippo','Ferrara'),(00000108,'Francesca.Mariani@mail.polimi.it','$2a$12$iG46HXRa7LoRwDQroLh/xevpyJubYtgp/kGMGcep1ahrg1OluDoZO','Francesca','Mariani'),(00000109,'Andrea.Mancini@mail.polimi.it','$2a$12$tDFUg5Xs8c6ErQMABeCxSuT7hlH.XEiGA96vsh8d7ERn.LL4DTcfu','Andrea','Mancini'),(00000110,'Edoardo.Longo@mail.polimi.it','$2a$12$A0TgGTiEdoU/MrkxIWzLTOOhQNWQcu90uIFZQhNNQG1Y7PorHGeQi','Edoardo','Longo'),(00000111,'Guido.Sartori@mail.polimi.it','$2a$12$aWYHs9d5zUk.OGKl50mr2eECaSJrHjYH41DFK38x87Ly5ThAXpWty','Guido','Sartori'),(00000112,'Francesca.Mazza@mail.polimi.it','$2a$12$Xb4siFNqLSX15bhcjkGV3O5Ns7J8/K8RoR1LyaF7nRuHtxM5KYp/y','Francesca','Mazza'),(00000113,'Paolo.Rossi@mail.polimi.it','$2a$12$gWoqhxwc2ks.0Ihru.ybkO3pJajpQBiadGeCPIeRuQScq3BaQR71a','Paolo','Rossi'),(00000114,'Davide.Bianco@mail.polimi.it','$2a$12$aEeRb.RIRc8rABRu3i7OZenRU.MhzuVBaVr0f/t44zbrZLzMx7Yey','Davide','Bianco'),(00000115,'Giovanni.Testa@mail.polimi.it','$2a$12$/eVYb2bs1hFx42njrWRQ5ufKQ4DGZRnjkRdDSJy4N8bPJkOouPe6e','Giovanni','Testa'),(00000116,'Tommaso.Giordano@mail.polimi.it','$2a$12$jl/6qQSy/W5nlSvumkv8xeHYmneRDM0IEUtbTZ8P1ZnDsm2fTP4OC','Tommaso','Giordano'),(00000117,'Sofia.Basile@mail.polimi.it','$2a$12$s38bgyza/s9oVveuDng/vOqCKyrg.YGA3dFdCTWyayQkFAdO/bWi2','Sofia','Basile'),(00000118,'Martina.Parodi@mail.polimi.it','$2a$12$UzqwGrMFFAdzOdFT7lhw6OgGSj0kVfDbYHHp3Kb7NakWwdGxPCjRa','Martina','Parodi'),(00000119,'Mattia.Serra@mail.polimi.it','$2a$12$.FdsL/tUQMsyvnRtzv9.FOeWIl.ZllVAc3VrePhNER/uHjVTPr7PG','Mattia','Serra'),(00000120,'Ginevra.Romano@mail.polimi.it','$2a$12$dVHPlszFS06v2kaoaTjBMuCrqXgqKiT5WR0OKke9G.4ddZdt8.NNy','Ginevra','Romano'),(00000121,'Matteo.Parodi@mail.polimi.it','$2a$12$fAc2t2r2OJHkbn9lRIOne.hqiONx5MQ1T3xplLe.Op/gx/jc.IvS6','Matteo','Parodi'),(00000122,'Daniele.Barbieri@mail.polimi.it','$2a$12$8kz0gplbTew30UYgut/LN.8AVVmSjjY6NNa1qCFcRSGQ6Zx9yVYA.','Daniele','Barbieri'),(00000123,'Niccolo.Bruno@mail.polimi.it','$2a$12$wyGuxYKDKkT0HRnnnQXKkeLjzu/wfqEkrhSDjaHbXEFcoq/aJQuX6','Niccolo','Bruno'),(00000124,'Sofia.Bianchi@mail.polimi.it','$2a$12$18.kgpeaiV/y4Nrgk6Ckp.cTWf4RgvpfimPb6lzuVwAjNvRpTaRZ2','Sofia','Bianchi'),(00000125,'Edoardo.Colombo@mail.polimi.it','$2a$12$W1wj675NlHcFwYvZKA4Yv.aLvWM1dov.dACVwPO5naxfffN9k5noC','Edoardo','Colombo'),(00000126,'Gaia.Ricci@mail.polimi.it','$2a$12$iOUSWKKjfe71F93rEB2PB.xwMOFsofJZP9c84Y9abHI1.giynfEP.','Gaia','Ricci'),(00000127,'Sofia.Serra@mail.polimi.it','$2a$12$pBz2k31b5vYOU6sYc4DEauRRhtj8zCb9ZvBiI1GI3sCALDrlavENu','Sofia','Serra'),(00000128,'Giuseppe.Pace@mail.polimi.it','$2a$12$ENBnwI78q3reQJM0fZ1mEulQWDYC.b0YiTDsxXVhDnH4Zxntghsw.','Giuseppe','Pace'),(00000129,'Alice.Romano@mail.polimi.it','$2a$12$JFJ0YBdx2FPV9Sht84Eb3eFDQYESSJoOSbi3DRZUic7QVMWZcPI.q','Alice','Romano'),(00000130,'Filippo.Marini@mail.polimi.it','$2a$12$PoiE6kBCFcx4BnGjfGynIuUO2NRAqgIY3IeOB9UxXpZ1xG7t2BIE2','Filippo','Marini');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-04-17 14:01:54
