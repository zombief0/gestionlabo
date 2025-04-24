-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le :  jeu. 26 mars 2020 à 08:08
-- Version du serveur :  5.7.21
-- Version de PHP :  5.6.35

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `gestionlabo`
--

DELIMITER $$
--
-- Procédures
--
DROP PROCEDURE IF EXISTS `genererFacture`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `genererFacture` (IN `idFacture` BIGINT)  NO SQL
SELECT facture.id_facture,examen.libelle as libelle_examen, examen.prix AS prix_examen, personne.nom AS nom, personne.prenom AS prenom,facture.somme AS somme_totale, examen.code as code, DATE_FORMAT(facture.date_creation_originale,'%Y-%m-%d %H:%i') AS date_creation_originale
FROM examen_souscrit,examen,personne,patient,facture
WHERE  examen_souscrit.examen_id_examen = examen.id_examen
AND examen_souscrit.patient_id_personne = patient.id_personne
AND patient.id_personne = personne.id_personne
AND facture.id_facture = examen_souscrit.facture_id_facture
AND facture.id_facture = idFacture$$

DROP PROCEDURE IF EXISTS `genererResultat`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `genererResultat` (IN `idCons` BIGINT)  NO SQL
SELECT personne.nom as nom_patient, personne.prenom as prenom_patient, personne.sexe AS sexe_patient,personne.telephone AS telephone,IF(TIMESTAMPDIFF(YEAR,personne.date,CURDATE()) = 0,CONCAT(TIMESTAMPDIFF(MONTH,personne.date,CURDATE()),' mois'),CONCAT(TIMESTAMPDIFF(YEAR,personne.date,CURDATE()),' an(s)')) AS age,DATE_FORMAT(consultation.date_consultation,'%Y-%m-%d') AS date_consultation,consultation.prescripteur AS prescripteur, laboratoire.libelle AS lab_lib,examen.libelle AS lib_examen, examen_souscrit.unite as unite, examen.max_valeur as max_valeur, examen.min_valeur AS min_valeur,  examen_souscrit.valeur_normale_patient AS valeur_patient 
FROM examen,examen_souscrit,consultation,patient,personne,laboratoire
WHERE examen.laboratoire_id_laboratoire = laboratoire.id_laboratoire
AND examen_souscrit.examen_id_examen = examen.id_examen
AND examen_souscrit.consultation_id_consultation = consultation.id_consultation
AND examen_souscrit.patient_id_personne = patient.id_personne
AND patient.id_personne = personne.id_personne
AND consultation.id_consultation = idCons$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `consultation`
--

DROP TABLE IF EXISTS `consultation`;
CREATE TABLE IF NOT EXISTS `consultation` (
  `id_consultation` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_consultation` datetime DEFAULT NULL,
  `prescripteur` varchar(255) NOT NULL,
  `statut` varchar(255) DEFAULT NULL,
  `patient_id_personne` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_consultation`),
  KEY `FK14lr8dcl5lc3bj4a35tll3m0b` (`patient_id_personne`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `consultation`
--

INSERT INTO `consultation` (`id_consultation`, `date_consultation`, `prescripteur`, `statut`, `patient_id_personne`) VALUES
(1, '2020-03-25 15:36:42', 'Mbom', 'EN_COURS', 2),
(2, '2020-03-25 16:32:59', 'Ntem', 'EN_COURS', 2),
(3, '2020-03-26 07:03:04', 'Ben', 'TERMINE', 2),
(5, '2020-03-26 08:02:30', 'Mbom', 'EN_COURS', 4);

-- --------------------------------------------------------

--
-- Structure de la table `examen`
--

DROP TABLE IF EXISTS `examen`;
CREATE TABLE IF NOT EXISTS `examen` (
  `id_examen` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `date_ajout` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `libelle` varchar(255) NOT NULL,
  `max_valeur` double NOT NULL,
  `min_valeur` double NOT NULL,
  `prix` double NOT NULL,
  `laboratoire_id_laboratoire` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_examen`),
  KEY `FK5oyxcsfaubgtg0ye8cx5vduwm` (`laboratoire_id_laboratoire`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `examen`
--

INSERT INTO `examen` (`id_examen`, `code`, `date_ajout`, `description`, `libelle`, `max_valeur`, `min_valeur`, `prix`, `laboratoire_id_laboratoire`) VALUES
(1, 'glu', '2020-03-25 16:30:21', '', 'Glucose', 0.1, 0.05, 2003, 1),
(2, 'chl', '2020-03-25 16:31:19', NULL, 'Chlore', 133, 98, 3000, 1),
(3, 'VIH', '2020-03-26 04:20:20', NULL, 'Virus Immuno Deficience', 0.86, 0.37, 2000, 2);

-- --------------------------------------------------------

--
-- Structure de la table `examen_souscrit`
--

DROP TABLE IF EXISTS `examen_souscrit`;
CREATE TABLE IF NOT EXISTS `examen_souscrit` (
  `id_examen_passer` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `valeur_normale_patient` double NOT NULL,
  `consultation_id_consultation` bigint(20) DEFAULT NULL,
  `examen_id_examen` bigint(20) DEFAULT NULL,
  `facture_id_facture` bigint(20) DEFAULT NULL,
  `patient_id_personne` bigint(20) DEFAULT NULL,
  `unite` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_examen_passer`),
  KEY `FKsbp5qtwjf0xfosyuphs8ynwrq` (`consultation_id_consultation`),
  KEY `FK10x2fmo3re3r17mw3b56hygnm` (`examen_id_examen`),
  KEY `FKl8johlcq0sixxe2dd7777wja4` (`facture_id_facture`),
  KEY `FK88tsyjxptahhp3xyvfe9yhsk9` (`patient_id_personne`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `examen_souscrit`
--

INSERT INTO `examen_souscrit` (`id_examen_passer`, `date`, `valeur_normale_patient`, `consultation_id_consultation`, `examen_id_examen`, `facture_id_facture`, `patient_id_personne`, `unite`) VALUES
(2, '2020-03-25 16:32:48', 0.2, 1, 1, 1, 2, 'g/l'),
(4, '2020-03-25 16:34:14', 0.63, 2, 1, NULL, 2, 'g/l'),
(5, '2020-03-26 04:22:31', 0.37, 2, 3, 1, 2, 'g/l'),
(6, '2020-03-26 05:53:26', 120, 1, 2, NULL, 2, 'mmol/l'),
(8, '2020-03-26 08:02:54', -1, 5, 3, NULL, 4, NULL),
(9, '2020-03-26 08:03:01', -1, 5, 2, NULL, 4, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `facture`
--

DROP TABLE IF EXISTS `facture`;
CREATE TABLE IF NOT EXISTS `facture` (
  `id_facture` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_creation_originale` datetime DEFAULT NULL,
  `date_creation_secondaire` datetime DEFAULT NULL,
  `somme` double NOT NULL,
  `utilisateur_id_personne` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_facture`),
  KEY `FKrbfmplb8yyeuembnrnxpq0j1k` (`utilisateur_id_personne`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `facture`
--

INSERT INTO `facture` (`id_facture`, `date_creation_originale`, `date_creation_secondaire`, `somme`, `utilisateur_id_personne`) VALUES
(1, '2020-03-26 05:54:29', '2020-03-26 06:17:30', 4003, 1);

-- --------------------------------------------------------

--
-- Structure de la table `laboratoire`
--

DROP TABLE IF EXISTS `laboratoire`;
CREATE TABLE IF NOT EXISTS `laboratoire` (
  `id_laboratoire` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `libelle` varchar(255) NOT NULL,
  PRIMARY KEY (`id_laboratoire`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `laboratoire`
--

INSERT INTO `laboratoire` (`id_laboratoire`, `description`, `libelle`) VALUES
(1, 'Où on teste les...', 'Biochimie'),
(2, '', 'Virologie');

-- --------------------------------------------------------

--
-- Structure de la table `patient`
--

DROP TABLE IF EXISTS `patient`;
CREATE TABLE IF NOT EXISTS `patient` (
  `date_enregistrement` datetime DEFAULT NULL,
  `date_modification` datetime DEFAULT NULL,
  `id_personne` bigint(20) NOT NULL,
  PRIMARY KEY (`id_personne`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `patient`
--

INSERT INTO `patient` (`date_enregistrement`, `date_modification`, `id_personne`) VALUES
('2020-03-25 15:36:26', '2020-03-25 15:36:26', 2),
('2020-03-26 08:02:18', '2020-03-26 08:02:18', 4);

-- --------------------------------------------------------

--
-- Structure de la table `personne`
--

DROP TABLE IF EXISTS `personne`;
CREATE TABLE IF NOT EXISTS `personne` (
  `id_personne` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `nom` varchar(255) NOT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  `sexe` varchar(255) DEFAULT NULL,
  `telephone` int(11) NOT NULL,
  PRIMARY KEY (`id_personne`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `personne`
--

INSERT INTO `personne` (`id_personne`, `date`, `nom`, `prenom`, `sexe`, `telephone`) VALUES
(1, '2020-03-25 15:34:15', '', '', 'MASCULIN', 664123587),
(2, '2016-12-21 23:00:00', 'Frejus', 'Norman', 'MASCULIN', 696528881),
(3, '2020-03-03 23:00:00', 'Ben', 'Tom', 'MASCULIN', 652478541),
(4, '2020-01-07 23:00:00', 'Jim', 'Norton', 'MASCULIN', 0);

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
CREATE TABLE IF NOT EXISTS `utilisateur` (
  `email` varchar(255) NOT NULL,
  `is_active` bit(1) NOT NULL,
  `mdp` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  `id_personne` bigint(20) NOT NULL,
  PRIMARY KEY (`id_personne`),
  UNIQUE KEY `UK_rma38wvnqfaf66vvmi57c71lo` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`email`, `is_active`, `mdp`, `role`, `id_personne`) VALUES
('root@mail.com', b'1', '$2a$10$.fztONrjpVV3uZlL7QhhIOmE1IzE2BNIvjQrcO3eOnmBXG4Ot/Due', 'ADMIN', 1),
('ben@gmail.com', b'1', '$2a$10$0zzWPPLvvAm5Ji3CyCWLIe706wIivDHmyieERM09cvsM7a4pSPWW2', 'MEDECIN', 3);

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `consultation`
--
ALTER TABLE `consultation`
  ADD CONSTRAINT `FK14lr8dcl5lc3bj4a35tll3m0b` FOREIGN KEY (`patient_id_personne`) REFERENCES `patient` (`id_personne`);

--
-- Contraintes pour la table `examen`
--
ALTER TABLE `examen`
  ADD CONSTRAINT `FK5oyxcsfaubgtg0ye8cx5vduwm` FOREIGN KEY (`laboratoire_id_laboratoire`) REFERENCES `laboratoire` (`id_laboratoire`);

--
-- Contraintes pour la table `examen_souscrit`
--
ALTER TABLE `examen_souscrit`
  ADD CONSTRAINT `FK10x2fmo3re3r17mw3b56hygnm` FOREIGN KEY (`examen_id_examen`) REFERENCES `examen` (`id_examen`),
  ADD CONSTRAINT `FK88tsyjxptahhp3xyvfe9yhsk9` FOREIGN KEY (`patient_id_personne`) REFERENCES `patient` (`id_personne`),
  ADD CONSTRAINT `FKl8johlcq0sixxe2dd7777wja4` FOREIGN KEY (`facture_id_facture`) REFERENCES `facture` (`id_facture`),
  ADD CONSTRAINT `FKsbp5qtwjf0xfosyuphs8ynwrq` FOREIGN KEY (`consultation_id_consultation`) REFERENCES `consultation` (`id_consultation`);

--
-- Contraintes pour la table `facture`
--
ALTER TABLE `facture`
  ADD CONSTRAINT `FKrbfmplb8yyeuembnrnxpq0j1k` FOREIGN KEY (`utilisateur_id_personne`) REFERENCES `utilisateur` (`id_personne`);

--
-- Contraintes pour la table `patient`
--
ALTER TABLE `patient`
  ADD CONSTRAINT `FKrjid09o84upv5kdr04t3jxwmu` FOREIGN KEY (`id_personne`) REFERENCES `personne` (`id_personne`);

--
-- Contraintes pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD CONSTRAINT `FK9xnoui07m786rl9fnnm4orvla` FOREIGN KEY (`id_personne`) REFERENCES `personne` (`id_personne`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
