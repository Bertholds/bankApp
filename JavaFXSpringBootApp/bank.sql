-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:3306
-- Généré le : sam. 04 sep. 2021 à 06:55
-- Version du serveur : 5.7.33
-- Version de PHP : 7.4.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `bank`
--

-- --------------------------------------------------------

--
-- Structure de la table `adherent`
--

CREATE TABLE `adherent` (
  `membre_id` bigint(20) NOT NULL,
  `date_naiss` date DEFAULT NULL,
  `fonction` varchar(255) DEFAULT NULL,
  `lieu_naiss` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  `situation` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `unique_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `adherent`
--

INSERT INTO `adherent` (`membre_id`, `date_naiss`, `fonction`, `lieu_naiss`, `nom`, `prenom`, `situation`, `type`, `unique_name`) VALUES
(1, '2021-07-21', 'tresorier', 'yaoundé', 'ndj', 'bk', 'Inactif', 'Membre', 'ndj bk'),
(2, '0020-01-11', 'Commissaire au compte', 'soa', 'Mafo Tala', 'Eugène Patout', 'Actif', 'Membre', 'Mafo Tala Eugène Patout'),
(3, '0021-01-10', 'Sanceur', 'Ebolowa', 'Atangana Atangana', 'Williams Coco', 'Inactif', 'Membre', 'Atangana Atangana Williams Coco'),
(4, '2015-09-03', 'Comptable', 'yaoundé', 'ndjanfa', 'bk', 'Actif', 'Membre', 'ndjanfa bk'),
(5, '1995-03-21', 'patern', 'patern', 'Test', 'patern', 'Trash', 'Membre', 'Test patern'),
(6, '2021-09-21', 'Test', 'Yaoundé', 'Koaumedjo Koaum', 'Kate Brayana', 'Actif', 'Membre', 'Koaumedjo Koaum Kate Brayana'),
(7, '1990-09-07', 'Membre', 'Douala', 'Baky King', 'Willy', 'Actif', 'Membre', 'Baky King Willy');

-- --------------------------------------------------------

--
-- Structure de la table `avalise`
--

CREATE TABLE `avalise` (
  `avalise_id` bigint(20) NOT NULL,
  `montant` float DEFAULT NULL,
  `remboursser` bit(1) NOT NULL,
  `solder` float DEFAULT NULL,
  `compte_creance_id_creance` bigint(20) DEFAULT NULL,
  `compte_epargne_epargne_id` bigint(20) DEFAULT NULL,
  `compte_tampon_id_tampon` bigint(20) DEFAULT NULL,
  `transaction_transaction_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `avalise`
--

INSERT INTO `avalise` (`avalise_id`, `montant`, `remboursser`, `solder`, `compte_creance_id_creance`, `compte_epargne_epargne_id`, `compte_tampon_id_tampon`, `transaction_transaction_id`) VALUES
(47, 1000, b'1', 1000, 6, 6, 4, 81),
(48, 1000, b'1', 1000, 7, 7, 4, 82),
(49, 5000, b'1', 5000, 4, 4, 6, 85),
(50, 5000, b'1', 5000, 4, 4, 6, 87),
(51, 5000, b'1', 5000, 4, 4, 6, 88),
(52, 2000, b'1', 2000, 4, 4, 6, 91),
(53, 2000, b'1', 2000, 4, 4, 6, 92),
(54, 3500, b'1', 3500, 4, 4, 6, 99),
(55, 1000, b'1', 1000, 6, 6, 4, 113),
(56, 2000, b'1', 2000, 6, 6, 4, 114);

-- --------------------------------------------------------

--
-- Structure de la table `compteepargne`
--

CREATE TABLE `compteepargne` (
  `epargne_id` bigint(20) NOT NULL,
  `avaliser` bit(1) NOT NULL,
  `fond` float NOT NULL,
  `lacarte` float NOT NULL,
  `solde` float NOT NULL,
  `adherent_membre_id` bigint(20) DEFAULT NULL,
  `statut` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `compteepargne`
--

INSERT INTO `compteepargne` (`epargne_id`, `avaliser`, `fond`, `lacarte`, `solde`, `adherent_membre_id`, `statut`) VALUES
(3, b'0', 0, 0, 0, 3, 'Inactif'),
(4, b'0', 0, 8000, 8000, 4, 'Actif'),
(6, b'0', 0, 19500, 19500, 6, 'Actif'),
(7, b'0', 0, 30000, 30000, 7, 'Actif');

-- --------------------------------------------------------

--
-- Structure de la table `comptetampon`
--

CREATE TABLE `comptetampon` (
  `id_tampon` bigint(20) NOT NULL,
  `dette` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `comptetampon`
--

INSERT INTO `comptetampon` (`id_tampon`, `dette`) VALUES
(4, 0),
(6, 0);

-- --------------------------------------------------------

--
-- Structure de la table `compteutilisateur`
--

CREATE TABLE `compteutilisateur` (
  `utilisateur_id` bigint(20) NOT NULL,
  `login` varchar(255) DEFAULT NULL,
  `niveau_access` varchar(255) DEFAULT NULL,
  `pass` varchar(255) DEFAULT NULL,
  `utilisateur_user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `compteutilisateur`
--

INSERT INTO `compteutilisateur` (`utilisateur_id`, `login`, `niveau_access`, `pass`, `utilisateur_user_id`) VALUES
(1, 'bk', 'Root', 'bk', 1),
(2, 'sd', 'Administrateur', 'sd', 2);

-- --------------------------------------------------------

--
-- Structure de la table `compte_creance`
--

CREATE TABLE `compte_creance` (
  `id_creance` bigint(20) NOT NULL,
  `montant` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `compte_creance`
--

INSERT INTO `compte_creance` (`id_creance`, `montant`) VALUES
(4, 0),
(6, 0),
(7, 0);

-- --------------------------------------------------------

--
-- Structure de la table `fond`
--

CREATE TABLE `fond` (
  `fond_id` bigint(20) NOT NULL,
  `solde` bigint(20) NOT NULL,
  `compte_epargne_epargne_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `menu`
--

CREATE TABLE `menu` (
  `id` bigint(20) NOT NULL,
  `droit_acces` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `menu`
--

INSERT INTO `menu` (`id`, `droit_acces`, `nom`) VALUES
(1, 'Root', 'Parametre'),
(2, '', NULL),
(3, 'Root', 'Adhérents'),
(4, 'Root', 'Comptes bancaire'),
(5, 'Root', 'Rapports'),
(6, 'Root', 'Transactions'),
(7, 'Root', 'Sauvegarder / Restaurer'),
(8, 'Root', 'Mochard'),
(9, 'Root', 'Utilisateurs');

-- --------------------------------------------------------

--
-- Structure de la table `operation`
--

CREATE TABLE `operation` (
  `id_operation` bigint(20) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `cible` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `utilisateur_user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `operation`
--

INSERT INTO `operation` (`id_operation`, `address`, `cible`, `date`, `name`, `utilisateur_user_id`) VALUES
(25, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 12:36:41', 'Operation de connection', 1),
(26, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 13:23:06', 'Operation de connection', 1),
(27, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 13:27:21', 'Operation de connection', 1),
(28, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 13:30:04', 'Operation de connection', 1),
(29, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 13:31:43', 'Operation de connection', 1),
(30, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 13:38:02', 'Operation de connection', 1),
(31, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 13:39:50', 'Operation de connection', 1),
(32, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 13:43:50', 'Operation de connection', 1),
(33, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 13:47:09', 'Operation de connection', 1),
(34, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 13:47:59', 'Operation de connection', 1),
(35, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 15:33:28', 'Operation de connection', 1),
(36, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 15:36:42', 'Operation de connection', 1),
(37, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 16:00:02', 'Operation de connection', 1),
(38, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 16:11:15', 'Operation de connection', 1),
(39, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 16:14:51', 'Operation de connection', 1),
(40, 'DESKTOP-6JPV481/192.168.1.247', '5-patern patern', '2021-09-02 16:16:08', 'Modification des informations d\'un adhérent', 1),
(41, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 16:21:48', 'Operation de connection', 1),
(42, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 16:29:43', 'Operation de connection', 1),
(43, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 16:33:58', 'Operation de connection', 1),
(44, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 16:36:42', 'Operation de connection', 1),
(45, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 16:42:14', 'Operation de connection', 1),
(46, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 16:43:06', 'Operation de connection', 1),
(47, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 17:07:10', 'Operation de connection', 1),
(48, 'DESKTOP-6JPV481/192.168.1.247', '5-Test patern', '2021-09-02 17:07:38', 'Modification des informations d\'un adhérent', 1),
(49, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 17:08:37', 'Operation de connection', 1),
(50, 'DESKTOP-6JPV481/192.168.1.247', '6-Koaumedjo Koaum Kate Brayana', '2021-09-02 17:09:59', 'Enregistrement d\'un adhérent', 1),
(51, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 17:11:01', 'Operation de connection', 1),
(52, 'DESKTOP-6JPV481/192.168.1.247', 'Test patern', '2021-09-02 17:13:36', 'Création du fond du compte bancaire', 1),
(53, 'DESKTOP-6JPV481/192.168.1.247', 'Test patern', '2021-09-02 17:19:43', 'Définir le fond du compte bancaire', 1),
(54, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 17:23:32', 'Operation de connection', 1),
(55, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-02 17:24:00', 'Création du fond du compte bancaire', 1),
(56, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-02 17:24:12', 'Définir le fond du compte bancaire', 1),
(57, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-02 17:24:22', 'Suppression du fond du compte bancaire', 1),
(58, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 18:09:03', 'Operation de connection', 1),
(59, 'DESKTOP-6JPV481/192.168.1.247', '1-ndj bk', '2021-09-02 18:09:43', 'Modification des informations d\'un adhérent', 1),
(60, 'DESKTOP-6JPV481/192.168.1.247', '2-Mafo Tala Eugène Patout', '2021-09-02 18:10:39', 'Modification des informations d\'un adhérent', 1),
(61, 'DESKTOP-6JPV481/192.168.1.247', '2-Mafo Tala Eugène Patout', '2021-09-02 18:10:49', 'Modification des informations d\'un adhérent', 1),
(62, 'DESKTOP-6JPV481/192.168.1.247', '3-Atangana Atangana Williams Coco', '2021-09-02 18:11:01', 'Modification des informations d\'un adhérent', 1),
(63, 'DESKTOP-6JPV481/192.168.1.247', '4-ndjanfa bk', '2021-09-02 18:11:30', 'Modification des informations d\'un adhérent', 1),
(64, 'DESKTOP-6JPV481/192.168.1.247', '5-Test patern', '2021-09-02 18:11:41', 'Modification des informations d\'un adhérent', 1),
(65, 'DESKTOP-6JPV481/192.168.1.247', '6-Koaumedjo Koaum Kate Brayana', '2021-09-02 18:11:51', 'Modification des informations d\'un adhérent', 1),
(66, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 18:16:30', 'Operation de connection', 1),
(67, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 18:18:17', 'Operation de connection', 1),
(68, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 18:20:44', 'Operation de connection', 1),
(69, 'DESKTOP-6JPV481/192.168.1.247', '4-ndjanfa bk', '2021-09-02 18:21:12', 'Modification des informations d\'un adhérent', 1),
(70, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 19:47:27', 'Operation de connection', 1),
(71, 'DESKTOP-6JPV481/192.168.1.247', 'ndj bk', '2021-09-02 19:49:48', 'Opération de depot', 1),
(72, 'DESKTOP-6JPV481/192.168.1.247', 'Atangana Atangana Williams Coco <<Emprinte a>> ndj bk', '2021-09-02 19:52:20', 'Opération de retrait', 1),
(73, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 20:24:30', 'Operation de connection', 1),
(74, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 20:27:29', 'Operation de connection', 1),
(75, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 20:32:07', 'Operation de connection', 1),
(76, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 20:37:10', 'Operation de connection', 1),
(77, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 20:42:01', 'Operation de connection', 1),
(78, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 20:49:59', 'Operation de connection', 1),
(79, 'DESKTOP-6JPV481/192.168.1.247', 'Mafo Tala Eugène Patout', '2021-09-02 21:05:01', 'Opération de depot', 1),
(80, 'DESKTOP-6JPV481/192.168.1.247', 'Test patern <<Emprinte a>> Mafo Tala Eugène Patout', '2021-09-02 21:06:25', 'Opération de retrait', 1),
(81, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 21:19:01', 'Operation de connection', 1),
(82, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 21:29:47', 'Operation de connection', 1),
(83, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 21:34:12', 'Operation de connection', 1),
(84, 'DESKTOP-6JPV481/192.168.1.247', 'Test patern', '2021-09-02 21:35:51', 'Suppression du fond du compte bancaire', 1),
(85, 'DESKTOP-6JPV481/192.168.1.247', 'Atangana Atangana Williams Coco', '2021-09-02 21:35:54', 'Suppression du fond du compte bancaire', 1),
(86, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-02 23:32:02', 'Operation de connection', 1),
(87, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 01:08:00', 'Operation de connection', 1),
(88, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 01:12:49', 'Operation de connection', 1),
(89, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 01:17:37', 'Operation de connection', 1),
(90, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 01:21:43', 'Operation de connection', 1),
(91, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 01:23:49', 'Operation de connection', 1),
(92, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 01:30:17', 'Operation de connection', 1),
(93, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 01:30:57', 'Operation de connection', 1),
(94, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 01:31:31', 'Operation de connection', 1),
(95, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 01:46:48', 'Operation de connection', 1),
(96, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 02:15:50', 'Operation de connection', 1),
(97, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 02:17:30', 'Operation de connection', 1),
(98, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 02:19:11', 'Operation de connection', 1),
(99, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 02:20:45', 'Operation de connection', 1),
(100, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 02:25:10', 'Operation de connection', 1),
(101, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 02:26:25', 'Operation de connection', 1),
(102, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 02:30:01', 'Operation de connection', 1),
(103, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 02:30:26', 'Operation de connection', 1),
(104, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 02:38:42', 'Operation de connection', 1),
(105, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 02:40:29', 'Operation de connection', 1),
(106, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 02:47:08', 'Operation de connection', 1),
(107, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 08:46:00', 'Operation de connection', 1),
(108, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 08:48:05', 'Operation de connection', 1),
(109, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 09:04:38', 'Operation de connection', 1),
(110, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 09:08:27', 'Operation de connection', 1),
(111, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 09:34:10', 'Operation de connection', 1),
(112, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 09:51:15', 'Operation de connection', 1),
(113, 'DESKTOP-6JPV481/192.168.1.247', '1-ndj bk', '2021-09-03 09:51:43', 'Modification des informations d\'un adhérent', 1),
(114, 'DESKTOP-6JPV481/192.168.1.247', '5-Test patern', '2021-09-03 09:51:58', 'Modification des informations d\'un adhérent', 1),
(115, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 09:57:58', 'Operation de connection', 1),
(116, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 10:01:41', 'Operation de connection', 1),
(117, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 10:08:29', 'Operation de connection', 1),
(118, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 10:16:06', 'Operation de connection', 1),
(119, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 10:23:12', 'Operation de connection', 1),
(120, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 10:28:13', 'Operation de connection', 1),
(121, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 10:29:23', 'Operation de connection', 1),
(122, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 10:36:16', 'Operation de connection', 1),
(123, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 10:48:46', 'Operation de connection', 1),
(124, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 10:51:17', 'Operation de connection', 1),
(125, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 10:53:17', 'Operation de connection', 1),
(126, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 11:03:39', 'Operation de connection', 1),
(127, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 11:09:13', 'Operation de connection', 1),
(128, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 12:19:10', 'Operation de connection', 1),
(129, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 13:22:57', 'Operation de connection', 1),
(130, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 13:32:53', 'Operation de connection', 1),
(131, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 13:43:36', 'Operation de connection', 1),
(132, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 13:44:56', 'Operation de connection', 1),
(133, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 13:55:26', 'Operation de connection', 1),
(134, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 14:04:52', 'Operation de connection', 1),
(135, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 14:07:49', 'Operation de connection', 1),
(136, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 14:09:00', 'Operation de connection', 1),
(137, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 14:40:35', 'Operation de connection', 1),
(138, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 15:17:56', 'Operation de connection', 1),
(139, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 15:27:17', 'Operation de connection', 1),
(140, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 15:29:20', 'Operation de connection', 1),
(141, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 15:33:38', 'Operation de connection', 1),
(142, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 15:50:00', 'Operation de connection', 1),
(143, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 15:53:07', 'Operation de connection', 1),
(144, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 16:00:30', 'Operation de connection', 1),
(145, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-03 16:01:57', 'Création du fond du compte bancaire', 1),
(146, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-03 16:02:13', 'Création du fond du compte bancaire', 1),
(147, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 16:10:48', 'Operation de connection', 1),
(148, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-03 16:11:14', 'Création du fond du compte bancaire', 1),
(149, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-03 16:11:21', 'Création du fond du compte bancaire', 1),
(150, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 16:40:17', 'Operation de connection', 1),
(151, 'DESKTOP-6JPV481/192.168.1.247', 'Mafo Tala Eugène Patout', '2021-09-03 16:48:29', 'Création du fond du compte bancaire', 1),
(152, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-03 16:48:40', 'Création du fond du compte bancaire', 1),
(153, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-03 16:48:45', 'Création du fond du compte bancaire', 1),
(154, 'DESKTOP-6JPV481/192.168.1.247', 'Mafo Tala Eugène Patout', '2021-09-03 16:50:45', 'Définir le fond du compte bancaire', 1),
(155, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-03 16:50:53', 'Définir le fond du compte bancaire', 1),
(156, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-03 16:51:01', 'Définir le fond du compte bancaire', 1),
(157, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-03 16:51:35', 'Opération de depot', 1),
(158, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-03 16:52:32', 'Opération de depot', 1),
(159, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 17:05:51', 'Operation de connection', 1),
(160, 'DESKTOP-6JPV481/192.168.1.247', 'Mafo Tala Eugène Patout', '2021-09-03 17:06:45', 'Opération de any', 1),
(161, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 17:12:02', 'Operation de connection', 1),
(162, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-03 17:12:34', 'Opération de any', 1),
(163, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-03 17:13:41', 'Opération de any', 1),
(164, 'DESKTOP-6JPV481/192.168.1.247', 'Mafo Tala Eugène Patout', '2021-09-03 17:15:33', 'Opération de any', 1),
(165, 'DESKTOP-6JPV481/192.168.1.247', 'Mafo Tala Eugène Patout', '2021-09-03 17:21:05', 'Opération de any', 1),
(166, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 17:37:17', 'Operation de connection', 1),
(167, 'DESKTOP-6JPV481/192.168.1.247', 'Mafo Tala Eugène Patout <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-03 17:38:11', 'Opération de retrait', 1),
(168, 'DESKTOP-6JPV481/192.168.1.247', 'Mafo Tala Eugène Patout <<Emprinte a>> ndjanfa bk', '2021-09-03 17:38:25', 'Opération de retrait', 1),
(169, 'DESKTOP-6JPV481/192.168.1.247', 'Mafo Tala Eugène Patout <<Emprinte a>> ndjanfa bk', '2021-09-03 17:38:42', 'Opération de retrait', 1),
(170, 'DESKTOP-6JPV481/192.168.1.247', 'Mafo Tala Eugène Patout <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-03 17:38:59', 'Opération de retrait', 1),
(171, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 18:00:02', 'Operation de connection', 1),
(172, 'DESKTOP-6JPV481/192.168.1.247', 'Mafo Tala Eugène Patout <<Emprinte a>> ndjanfa bk', '2021-09-03 18:01:09', 'Opération de retrait', 1),
(173, 'DESKTOP-6JPV481/192.168.1.247', 'Mafo Tala Eugène Patout <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-03 18:02:52', 'Opération de retrait', 1),
(174, 'DESKTOP-6JPV481/192.168.1.247', 'bn', '2021-09-03 18:26:08', 'Edition d\'utilisateur', 1),
(175, 'DESKTOP-6JPV481/192.168.1.247', 'bk', '2021-09-03 18:26:16', 'Edition d\'utilisateur', 1),
(176, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 18:31:28', 'Operation de connection', 1),
(177, 'DESKTOP-6JPV481/192.168.1.247', 'tt', '2021-09-03 18:31:45', 'Creation de compte utilisateur', 1),
(178, 'DESKTOP-6JPV481/192.168.1.247', 'tt', '2021-09-03 18:32:05', 'Creation de compte utilisateur', 1),
(179, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-03 23:24:59', 'Operation de connection', 5),
(180, 'DESKTOP-6JPV481/192.168.1.247', '7-Baky King Willy', '2021-09-03 23:27:55', 'Enregistrement d\'un adhérent', 5),
(181, 'DESKTOP-6JPV481/192.168.1.247', 'Atangana Atangana Williams Coco', '2021-09-03 23:30:08', 'Création du fond du compte bancaire', 5),
(182, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-03 23:37:34', 'Création du fond du compte bancaire', 5),
(183, 'DESKTOP-6JPV481/192.168.1.247', 'Baky King Willy', '2021-09-03 23:37:40', 'Création du fond du compte bancaire', 5),
(184, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-03 23:38:05', 'Création du fond du compte bancaire', 5),
(185, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-03 23:39:08', 'Opération de any', 5),
(186, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-03 23:39:27', 'Opération de any', 5),
(187, 'DESKTOP-6JPV481/192.168.1.247', 'Baky King Willy', '2021-09-03 23:39:41', 'Opération de any', 5),
(188, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-03 23:40:32', 'Opération de any', 5),
(189, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-03 23:41:27', 'Définir le fond du compte bancaire', 5),
(190, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-03 23:42:56', 'Définir le fond du compte bancaire', 5),
(191, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Baky King Willy', '2021-09-03 23:44:45', 'Opération de retrait', 5),
(192, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-03 23:45:03', 'Opération de retrait', 5),
(193, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 00:25:19', 'Opération de retrait', 5),
(194, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Baky King Willy', '2021-09-04 00:25:45', 'Opération de retrait', 5),
(195, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 00:27:34', 'Operation de connection', 1),
(196, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 00:28:16', 'Opération de retrait', 1),
(197, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Baky King Willy', '2021-09-04 00:28:20', 'Opération de retrait', 1),
(198, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 00:31:22', 'Operation de connection', 1),
(199, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 00:32:46', 'Opération de retrait', 1),
(200, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 00:34:19', 'Operation de connection', 1),
(201, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 00:35:06', 'Opération de retrait', 1),
(202, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 00:37:35', 'Operation de connection', 1),
(203, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 00:38:42', 'Opération de retrait', 1),
(204, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 00:41:17', 'Opération de retrait', 1),
(205, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 00:48:12', 'Operation de connection', 1),
(206, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 00:48:51', 'Opération de retrait', 1),
(207, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 00:59:08', 'Operation de connection', 1),
(208, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 01:01:07', 'Operation de connection', 1),
(209, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 01:02:10', 'Opération de retrait', 1),
(210, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 01:06:11', 'Operation de connection', 1),
(211, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-04 01:06:35', 'Création du fond du compte bancaire', 1),
(212, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 01:06:40', 'Création du fond du compte bancaire', 1),
(213, 'DESKTOP-6JPV481/192.168.1.247', 'Baky King Willy', '2021-09-04 01:06:44', 'Création du fond du compte bancaire', 1),
(214, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-04 01:07:03', 'Définir le fond du compte bancaire', 1),
(215, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 01:07:11', 'Définir le fond du compte bancaire', 1),
(216, 'DESKTOP-6JPV481/192.168.1.247', 'Baky King Willy', '2021-09-04 01:07:16', 'Définir le fond du compte bancaire', 1),
(217, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-04 01:07:40', 'Opération de any', 1),
(218, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 01:07:50', 'Opération de any', 1),
(219, 'DESKTOP-6JPV481/192.168.1.247', 'Baky King Willy', '2021-09-04 01:08:00', 'Opération de any', 1),
(220, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-04 01:08:17', 'Opération de any', 1),
(221, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-04 01:08:59', 'Opération de any', 1),
(222, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-04 01:09:20', 'Opération de any', 1),
(223, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 01:09:42', 'Opération de retrait', 1),
(224, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Baky King Willy', '2021-09-04 01:09:50', 'Opération de retrait', 1),
(225, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Baky King Willy', '2021-09-04 01:10:11', 'Opération de retrait', 1),
(226, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Baky King Willy', '2021-09-04 01:10:32', 'Opération de retrait', 1),
(227, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Baky King Willy', '2021-09-04 01:10:49', 'Opération de retrait', 1),
(228, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Baky King Willy', '2021-09-04 01:12:19', 'Opération de retrait', 1),
(229, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Baky King Willy', '2021-09-04 01:12:27', 'Opération de retrait', 1),
(230, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Baky King Willy', '2021-09-04 01:12:53', 'Opération de retrait', 1),
(231, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Baky King Willy', '2021-09-04 01:14:56', 'Opération de retrait', 1),
(232, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 01:28:12', 'Opération de retrait', 1),
(233, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Baky King Willy', '2021-09-04 01:29:04', 'Opération de retrait', 1),
(234, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Baky King Willy', '2021-09-04 01:29:13', 'Opération de retrait', 1),
(235, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 01:34:22', 'Operation de connection', 1),
(236, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-04 01:34:32', 'Création du fond du compte bancaire', 1),
(237, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 01:34:36', 'Création du fond du compte bancaire', 1),
(238, 'DESKTOP-6JPV481/192.168.1.247', 'Baky King Willy', '2021-09-04 01:34:40', 'Création du fond du compte bancaire', 1),
(239, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-04 01:35:02', 'Opération de any', 1),
(240, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 01:35:14', 'Opération de any', 1),
(241, 'DESKTOP-6JPV481/192.168.1.247', 'Baky King Willy', '2021-09-04 01:35:24', 'Opération de any', 1),
(242, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-04 01:35:35', 'Opération de any', 1),
(243, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 01:36:12', 'Opération de retrait', 1),
(244, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 01:43:31', 'Operation de connection', 1),
(245, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 01:44:06', 'Opération de retrait', 1),
(246, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 01:44:17', 'Opération de retrait', 1),
(247, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 01:48:11', 'Opération de retrait', 1),
(248, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Baky King Willy', '2021-09-04 01:48:17', 'Opération de retrait', 1),
(249, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Baky King Willy', '2021-09-04 01:48:21', 'Opération de retrait', 1),
(250, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 01:57:56', 'Operation de connection', 1),
(251, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 01:58:40', 'Opération de retrait', 1),
(252, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 02:01:20', 'Operation de connection', 1),
(253, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 02:01:52', 'Opération de retrait', 1),
(254, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 02:02:12', 'Opération de retrait', 1),
(255, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 02:03:36', 'Operation de connection', 1),
(256, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-04 02:03:47', 'Création du fond du compte bancaire', 1),
(257, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 02:03:51', 'Création du fond du compte bancaire', 1),
(258, 'DESKTOP-6JPV481/192.168.1.247', 'Baky King Willy', '2021-09-04 02:03:54', 'Création du fond du compte bancaire', 1),
(259, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-04 02:04:19', 'Opération de any', 1),
(260, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 02:04:29', 'Opération de any', 1),
(261, 'DESKTOP-6JPV481/192.168.1.247', 'Baky King Willy', '2021-09-04 02:04:36', 'Opération de any', 1),
(262, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-04 02:04:46', 'Opération de any', 1),
(263, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 02:11:29', 'Operation de connection', 1),
(264, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 02:12:38', 'Opération de retrait', 1),
(265, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 02:13:15', 'Opération de retrait', 1),
(266, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 02:23:50', 'Operation de connection', 1),
(267, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 02:24:37', 'Opération de retrait', 1),
(268, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 02:28:22', 'Operation de connection', 1),
(269, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 02:29:21', 'Opération de retrait', 1),
(270, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 02:33:26', 'Operation de connection', 1),
(271, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-04 02:33:36', 'Création du fond du compte bancaire', 1),
(272, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 02:33:42', 'Création du fond du compte bancaire', 1),
(273, 'DESKTOP-6JPV481/192.168.1.247', 'Baky King Willy', '2021-09-04 02:33:46', 'Création du fond du compte bancaire', 1),
(274, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-04 02:34:11', 'Opération de any', 1),
(275, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 02:34:28', 'Opération de any', 1),
(276, 'DESKTOP-6JPV481/192.168.1.247', 'Baky King Willy', '2021-09-04 02:34:37', 'Opération de any', 1),
(277, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-04 02:34:48', 'Opération de any', 1),
(278, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 02:35:22', 'Opération de retrait', 1),
(279, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Baky King Willy', '2021-09-04 02:35:27', 'Opération de retrait', 1),
(280, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<remboursse a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 02:39:44', 'Opération de depot', 1),
(281, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk-->Baky King Willy', '2021-09-04 02:40:50', 'Opération de depot', 1),
(282, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk-->Koaumedjo Koaum Kate Brayana', '2021-09-04 02:43:59', 'Opération de depot', 1),
(283, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk-->Baky King Willy', '2021-09-04 02:44:11', 'Opération de depot', 1),
(284, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-04 02:45:21', 'Opération de any', 1),
(285, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana <<Emprinte a>> ndjanfa bk', '2021-09-04 02:46:03', 'Opération de retrait', 1),
(286, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana-->ndjanfa bk', '2021-09-04 02:47:47', 'Opération de any', 1),
(287, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana-->ndjanfa bk', '2021-09-04 02:48:56', 'Opération de virement', 1),
(288, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana-->ndjanfa bk', '2021-09-04 02:49:08', 'Opération de virement', 1),
(289, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana <<Emprinte a>> ndjanfa bk', '2021-09-04 02:50:28', 'Opération de retrait', 1),
(290, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana-->ndjanfa bk', '2021-09-04 03:03:17', 'Opération de virement', 1),
(291, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 03:03:51', 'Operation de connection', 1),
(292, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana <<Emprinte a>> ndjanfa bk', '2021-09-04 03:04:57', 'Opération de retrait', 1),
(293, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana <<remboursse a>> ndjanfa bk', '2021-09-04 03:06:31', 'Opération de depot', 1),
(294, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 03:10:06', 'Opération de any', 1),
(295, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana-->ndjanfa bk', '2021-09-04 03:10:57', 'Opération de depot', 1),
(296, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana <<Emprinte a>> ndjanfa bk', '2021-09-04 03:31:25', 'Opération de retrait', 1),
(297, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana <<Emprinte a>> ndjanfa bk', '2021-09-04 03:33:12', 'Opération de retrait', 1),
(298, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 03:34:45', 'Opération de any', 1),
(299, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 03:37:23', 'Operation de connection', 1),
(300, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 03:38:16', 'Opération de any', 1),
(301, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana <<remboursse a>> ndjanfa bk', '2021-09-04 03:39:11', 'Opération de depot', 1),
(302, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 03:56:54', 'Operation de connection', 1),
(303, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana <<remboursse a>> ndjanfa bk', '2021-09-04 03:57:26', 'Opération de depot', 1),
(304, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana-->ndjanfa bk', '2021-09-04 03:58:14', 'Opération de any', 1),
(305, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana-->ndjanfa bk', '2021-09-04 03:59:19', 'Opération de any', 1),
(306, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana-->ndjanfa bk', '2021-09-04 04:00:06', 'Opération de virement', 1),
(307, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 04:07:26', 'Operation de connection', 1),
(308, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana <<Emprinte a>> ndjanfa bk', '2021-09-04 04:08:09', 'Opération de retrait', 1),
(309, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 04:08:55', 'Opération de any', 1),
(310, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 04:10:24', 'Opération de any', 1),
(311, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 04:12:15', 'Operation de connection', 1),
(312, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 04:12:41', 'Opération de any', 1),
(313, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 04:13:41', 'Opération de any', 1),
(314, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 04:16:13', 'Operation de connection', 1),
(315, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 04:16:32', 'Opération de any', 1),
(316, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 04:17:07', 'Opération de any', 1),
(317, 'DESKTOP-6JPV481/192.168.1.247', 'Baky King Willy', '2021-09-04 04:18:36', 'Opération de any', 1),
(318, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 04:26:15', 'Opération de any', 1),
(319, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana', '2021-09-04 04:27:21', 'Opération de any', 1),
(320, 'DESKTOP-6JPV481/192.168.1.247', 'Koaumedjo Koaum Kate Brayana <<remboursse a>> ndjanfa bk', '2021-09-04 04:28:48', 'Opération de virement', 1),
(321, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-04 04:30:24', 'Opération de any', 1),
(322, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-04 04:32:56', 'Opération de any', 1),
(323, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk', '2021-09-04 04:33:43', 'Opération de any', 1),
(324, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 04:34:26', 'Opération de retrait', 1),
(325, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk-->Koaumedjo Koaum Kate Brayana', '2021-09-04 04:36:39', 'Opération de virement', 1),
(326, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk <<Emprinte a>> Koaumedjo Koaum Kate Brayana', '2021-09-04 04:37:45', 'Opération de retrait', 1),
(327, 'DESKTOP-6JPV481/192.168.1.247', 'ndjanfa bk-->Koaumedjo Koaum Kate Brayana', '2021-09-04 04:38:36', 'Opération de depot', 1),
(328, 'DESKTOP-6JPV481/192.168.1.247', 'Atangana Atangana Williams Coco', '2021-09-04 04:42:46', 'Création du fond du compte bancaire', 1),
(329, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 04:59:38', 'Operation de connection', 1),
(330, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 05:07:24', 'Operation de connection', 1),
(331, 'DESKTOP-6JPV481/192.168.1.247', 'mf', '2021-09-04 05:08:00', 'Creation de compte utilisateur', 1),
(332, 'DESKTOP-6JPV481/192.168.1.247', 'mf', '2021-09-04 05:09:28', 'Creation de compte utilisateur', 1),
(333, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 05:17:13', 'Operation de connection', 1),
(334, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 05:18:20', 'Operation de connection', 2),
(335, 'DESKTOP-6JPV481/192.168.1.247', 'Pas de cible', '2021-09-04 05:19:18', 'Operation de connection', 1);

-- --------------------------------------------------------

--
-- Structure de la table `transaction`
--

CREATE TABLE `transaction` (
  `transaction_id` bigint(20) NOT NULL,
  `date` datetime DEFAULT NULL,
  `montant` float DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `adherent_membre_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `transaction`
--

INSERT INTO `transaction` (`transaction_id`, `date`, `montant`, `type`, `adherent_membre_id`) VALUES
(8, '2021-09-03 16:51:34', 25000, 'depot', 4),
(9, '2021-09-03 16:52:31', 25000, 'depot', 6),
(10, '2021-09-03 17:06:44', 25000, 'depot', 2),
(11, '2021-09-03 17:12:33', 5000, 'depot', 4),
(12, '2021-09-03 17:13:40', 5000, 'depot', 6),
(13, '2021-09-03 17:15:32', 5000, 'depot', 2),
(14, '2021-09-03 17:21:04', 20000, 'retrait', 2),
(15, '2021-09-03 17:38:09', 5000, 'retrait', 2),
(16, '2021-09-03 17:38:23', 5000, 'retrait', 2),
(17, '2021-09-03 17:38:29', 5000, 'retrait', 2),
(18, '2021-09-03 17:38:56', 5000, 'retrait', 2),
(19, '2021-09-03 18:01:07', 5000, 'retrait', 2),
(20, '2021-09-03 18:02:49', 5000, 'retrait', 2),
(21, '2021-09-03 23:39:07', 25000, 'depot', 4),
(22, '2021-09-03 23:39:26', 25000, 'depot', 6),
(23, '2021-09-03 23:39:39', 25000, 'depot', 7),
(24, '2021-09-03 23:40:31', 15000, 'retrait', 4),
(25, '2021-09-03 23:44:41', 2500, 'retrait', 4),
(26, '2021-09-03 23:44:55', 2500, 'retrait', 4),
(27, '2021-09-04 00:25:17', 1000, 'retrait', 4),
(28, '2021-09-04 00:25:43', 1000, 'retrait', 4),
(29, '2021-09-04 00:28:14', 500, 'retrait', 4),
(30, '2021-09-04 00:28:17', 500, 'retrait', 4),
(31, '2021-09-04 00:32:43', 500, 'retrait', 4),
(32, '2021-09-04 00:35:04', 500, 'retrait', 4),
(33, '2021-09-04 00:38:40', 1000, 'retrait', 4),
(34, '2021-09-04 00:41:13', 1000, 'retrait', 4),
(35, '2021-09-04 00:48:39', 500, 'retrait', 4),
(36, '2021-09-04 00:59:32', 500, 'retrait', 4),
(37, '2021-09-04 01:02:07', 2000, 'retrait', 4),
(38, '2021-09-04 01:07:39', 25000, 'depot', 4),
(39, '2021-09-04 01:07:49', 25000, 'depot', 6),
(40, '2021-09-04 01:07:59', 25000, 'depot', 7),
(41, '2021-09-04 01:08:16', 25000, 'retrait', 4),
(42, '2021-09-04 01:08:58', 25000, 'depot', 4),
(43, '2021-09-04 01:09:19', 15000, 'retrait', 4),
(44, '2021-09-04 01:09:40', 1000, 'retrait', 4),
(45, '2021-09-04 01:09:47', 1000, 'retrait', 4),
(46, '2021-09-04 01:09:55', 1, 'retrait', 4),
(47, '2021-09-04 01:10:27', 200, 'retrait', 4),
(48, '2021-09-04 01:10:47', 2000, 'retrait', 4),
(49, '2021-09-04 01:12:17', 200, 'retrait', 4),
(50, '2021-09-04 01:12:25', 1000, 'retrait', 4),
(51, '2021-09-04 01:12:47', 2000, 'retrait', 4),
(52, '2021-09-04 01:14:54', 200, 'retrait', 4),
(53, '2021-09-04 01:28:09', 2600, 'retrait', 4),
(54, '2021-09-04 01:29:00', 2600, 'retrait', 4),
(55, '2021-09-04 01:29:11', 2500, 'retrait', 4),
(56, '2021-09-04 01:35:01', 25000, 'depot', 4),
(57, '2021-09-04 01:35:13', 25000, 'depot', 6),
(58, '2021-09-04 01:35:23', 25000, 'depot', 7),
(59, '2021-09-04 01:35:34', 15000, 'retrait', 4),
(60, '2021-09-04 01:36:09', 1000, 'retrait', 4),
(61, '2021-09-04 01:44:02', 2000, 'retrait', 4),
(62, '2021-09-04 01:44:15', 1000, 'retrait', 4),
(63, '2021-09-04 01:48:09', 2000, 'retrait', 4),
(64, '2021-09-04 01:48:15', 2000, 'retrait', 4),
(65, '2021-09-04 01:48:19', 2000, 'retrait', 4),
(66, '2021-09-04 01:58:37', 5000, 'retrait', 4),
(67, '2021-09-04 02:01:45', 2001, 'retrait', 4),
(68, '2021-09-04 02:02:06', 5000, 'retrait', 4),
(69, '2021-09-04 02:04:18', 25000, 'depot', 4),
(70, '2021-09-04 02:04:28', 25000, 'depot', 6),
(71, '2021-09-04 02:04:36', 25000, 'depot', 7),
(72, '2021-09-04 02:04:45', 15000, 'retrait', 4),
(73, '2021-09-04 02:12:33', 2000, 'retrait', 4),
(74, '2021-09-04 02:13:13', 0, 'retrait', 4),
(75, '2021-09-04 02:24:35', 4000, 'retrait', 4),
(76, '2021-09-04 02:29:19', 1000, 'retrait', 4),
(77, '2021-09-04 02:34:10', 25000, 'depot', 4),
(78, '2021-09-04 02:34:26', 25000, 'depot', 6),
(79, '2021-09-04 02:34:36', 25000, 'depot', 7),
(80, '2021-09-04 02:34:47', 15000, 'retrait', 4),
(81, '2021-09-04 02:35:19', 1000, 'retrait', 4),
(82, '2021-09-04 02:35:25', 1000, 'retrait', 4),
(83, '2021-09-04 02:39:42', 12000, 'depot', 4),
(84, '2021-09-04 02:45:20', 23000, 'depot', 4),
(85, '2021-09-04 02:46:01', 5000, 'retrait', 6),
(86, '2021-09-04 02:47:41', 5000, 'virement', 6),
(87, '2021-09-04 02:50:26', 5000, 'retrait', 6),
(88, '2021-09-04 03:04:55', 5000, 'retrait', 6),
(89, '2021-09-04 03:06:28', 5000, 'depot', 6),
(90, '2021-09-04 03:10:06', 20000, 'retrait', 6),
(91, '2021-09-04 03:31:22', 2000, 'retrait', 6),
(92, '2021-09-04 03:33:10', 2000, 'retrait', 6),
(93, '2021-09-04 03:34:43', 2000, 'depot', 6),
(94, '2021-09-04 03:38:15', 2000, 'depot', 6),
(95, '2021-09-04 03:39:06', 2000, 'depot', 6),
(96, '2021-09-04 03:57:23', 2000, 'depot', 6),
(97, '2021-09-04 03:58:13', 1000, 'virement', 6),
(98, '2021-09-04 03:59:18', 500, 'virement', 6),
(99, '2021-09-04 04:08:07', 3500, 'retrait', 6),
(100, '2021-09-04 04:08:54', 10000, 'depot', 6),
(101, '2021-09-04 04:10:23', 8500, 'depot', 6),
(102, '2021-09-04 04:12:40', 500, 'depot', 6),
(103, '2021-09-04 04:13:39', 500, 'depot', 6),
(104, '2021-09-04 04:16:30', 500, 'depot', 6),
(105, '2021-09-04 04:17:06', 500, 'depot', 6),
(106, '2021-09-04 04:18:35', 5000, 'depot', 7),
(107, '2021-09-04 04:26:13', 500, 'depot', 6),
(108, '2021-09-04 04:27:20', 500, 'depot', 6),
(109, '2021-09-04 04:28:46', 6500, 'virement', 6),
(110, '2021-09-04 04:30:20', 20000, 'retrait', 4),
(111, '2021-09-04 04:32:55', 9500, 'retrait', 4),
(112, '2021-09-04 04:33:41', 500, 'retrait', 4),
(113, '2021-09-04 04:34:23', 1000, 'retrait', 4),
(114, '2021-09-04 04:37:43', 2000, 'retrait', 4);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `dob` tinyblob,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `user_id` bigint(20) NOT NULL,
  `pseudo` varchar(255) DEFAULT NULL,
  `membre_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`user_id`, `pseudo`, `membre_id`) VALUES
(1, 'bk', 1),
(2, 'mf', 2),
(5, 'tt', 5);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `adherent`
--
ALTER TABLE `adherent`
  ADD PRIMARY KEY (`membre_id`);

--
-- Index pour la table `avalise`
--
ALTER TABLE `avalise`
  ADD PRIMARY KEY (`avalise_id`),
  ADD KEY `FKhr410dhc01lxc04ojy33ut3ws` (`compte_creance_id_creance`),
  ADD KEY `FK2larn9p8xfgsa09bta5dkwcm` (`compte_epargne_epargne_id`),
  ADD KEY `FK62ux5xfbfckmttxxpews2evxa` (`compte_tampon_id_tampon`),
  ADD KEY `FKtmwkhuxnokv3oahnx33mb018a` (`transaction_transaction_id`);

--
-- Index pour la table `compteepargne`
--
ALTER TABLE `compteepargne`
  ADD PRIMARY KEY (`epargne_id`),
  ADD KEY `FKakqeimeme5rjqx373gwgej53s` (`adherent_membre_id`);

--
-- Index pour la table `comptetampon`
--
ALTER TABLE `comptetampon`
  ADD PRIMARY KEY (`id_tampon`);

--
-- Index pour la table `compteutilisateur`
--
ALTER TABLE `compteutilisateur`
  ADD PRIMARY KEY (`utilisateur_id`),
  ADD KEY `FKovi8g3up0bcdyae8ecv0ojc99` (`utilisateur_user_id`);

--
-- Index pour la table `compte_creance`
--
ALTER TABLE `compte_creance`
  ADD PRIMARY KEY (`id_creance`);

--
-- Index pour la table `fond`
--
ALTER TABLE `fond`
  ADD PRIMARY KEY (`fond_id`),
  ADD KEY `FKdeoe1274t6axx7777abmh3cbk` (`compte_epargne_epargne_id`);

--
-- Index pour la table `menu`
--
ALTER TABLE `menu`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `operation`
--
ALTER TABLE `operation`
  ADD PRIMARY KEY (`id_operation`),
  ADD KEY `FKor80ek22m2h52vinr0rltws4o` (`utilisateur_user_id`);

--
-- Index pour la table `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `FK9y1h3j5oocgmmx1xnsfm5oxj1` (`adherent_membre_id`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`user_id`),
  ADD KEY `FKm3wxmj1mhs46cn5chsegkofl5` (`membre_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `adherent`
--
ALTER TABLE `adherent`
  MODIFY `membre_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `avalise`
--
ALTER TABLE `avalise`
  MODIFY `avalise_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=57;

--
-- AUTO_INCREMENT pour la table `fond`
--
ALTER TABLE `fond`
  MODIFY `fond_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `menu`
--
ALTER TABLE `menu`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT pour la table `operation`
--
ALTER TABLE `operation`
  MODIFY `id_operation` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=336;

--
-- AUTO_INCREMENT pour la table `transaction`
--
ALTER TABLE `transaction`
  MODIFY `transaction_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=115;

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `avalise`
--
ALTER TABLE `avalise`
  ADD CONSTRAINT `FK2larn9p8xfgsa09bta5dkwcm` FOREIGN KEY (`compte_epargne_epargne_id`) REFERENCES `compteepargne` (`epargne_id`),
  ADD CONSTRAINT `FK62ux5xfbfckmttxxpews2evxa` FOREIGN KEY (`compte_tampon_id_tampon`) REFERENCES `comptetampon` (`id_tampon`),
  ADD CONSTRAINT `FKhr410dhc01lxc04ojy33ut3ws` FOREIGN KEY (`compte_creance_id_creance`) REFERENCES `compte_creance` (`id_creance`),
  ADD CONSTRAINT `FKtmwkhuxnokv3oahnx33mb018a` FOREIGN KEY (`transaction_transaction_id`) REFERENCES `transaction` (`transaction_id`);

--
-- Contraintes pour la table `compteepargne`
--
ALTER TABLE `compteepargne`
  ADD CONSTRAINT `FKakqeimeme5rjqx373gwgej53s` FOREIGN KEY (`adherent_membre_id`) REFERENCES `adherent` (`membre_id`);

--
-- Contraintes pour la table `compteutilisateur`
--
ALTER TABLE `compteutilisateur`
  ADD CONSTRAINT `FKovi8g3up0bcdyae8ecv0ojc99` FOREIGN KEY (`utilisateur_user_id`) REFERENCES `utilisateur` (`user_id`);

--
-- Contraintes pour la table `fond`
--
ALTER TABLE `fond`
  ADD CONSTRAINT `FKdeoe1274t6axx7777abmh3cbk` FOREIGN KEY (`compte_epargne_epargne_id`) REFERENCES `compteepargne` (`epargne_id`);

--
-- Contraintes pour la table `operation`
--
ALTER TABLE `operation`
  ADD CONSTRAINT `FKor80ek22m2h52vinr0rltws4o` FOREIGN KEY (`utilisateur_user_id`) REFERENCES `utilisateur` (`user_id`);

--
-- Contraintes pour la table `transaction`
--
ALTER TABLE `transaction`
  ADD CONSTRAINT `FK9y1h3j5oocgmmx1xnsfm5oxj1` FOREIGN KEY (`adherent_membre_id`) REFERENCES `adherent` (`membre_id`);

--
-- Contraintes pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD CONSTRAINT `FKm3wxmj1mhs46cn5chsegkofl5` FOREIGN KEY (`membre_id`) REFERENCES `adherent` (`membre_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
