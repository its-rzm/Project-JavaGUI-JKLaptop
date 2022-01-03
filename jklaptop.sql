-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 19 Des 2021 pada 16.21
-- Versi server: 10.4.19-MariaDB
-- Versi PHP: 8.0.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `jklaptop`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `brand`
--

CREATE TABLE `brand` (
  `brandId` varchar(50) NOT NULL,
  `brandName` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `brand`
--

INSERT INTO `brand` (`brandId`, `brandName`) VALUES
('BD001', 'ASUS'),
('BD002', 'DELLO'),
('BD736', 'LENOVO');

-- --------------------------------------------------------

--
-- Struktur dari tabel `cart`
--

CREATE TABLE `cart` (
  `userId` varchar(50) NOT NULL,
  `productId` varchar(50) NOT NULL,
  `qty` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktur dari tabel `detailtransaction`
--

CREATE TABLE `detailtransaction` (
  `transactionId` varchar(50) NOT NULL,
  `productId` varchar(50) NOT NULL,
  `qty` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `detailtransaction`
--

INSERT INTO `detailtransaction` (`transactionId`, `productId`, `qty`) VALUES
('TR797', 'PD695', 2),
('TR797', 'PD067', 1),
('TR797', 'PD695', 1),
('TR797', 'PD102', 2),
('TR797', 'PD107', 1),
('TR845', 'PD695', 1);

-- --------------------------------------------------------

--
-- Struktur dari tabel `headertransaction`
--

CREATE TABLE `headertransaction` (
  `transactionId` varchar(50) NOT NULL,
  `userId` varchar(50) NOT NULL,
  `transactionDate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `headertransaction`
--

INSERT INTO `headertransaction` (`transactionId`, `userId`, `transactionDate`) VALUES
('TR797', 'US130', '2021-12-19'),
('TR845', 'US130', '2021-12-19');

-- --------------------------------------------------------

--
-- Struktur dari tabel `product`
--

CREATE TABLE `product` (
  `productId` varchar(50) NOT NULL,
  `brandId` varchar(50) NOT NULL,
  `productName` varchar(50) NOT NULL,
  `productPrice` int(10) NOT NULL,
  `productStock` int(10) NOT NULL,
  `productRating` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `product`
--

INSERT INTO `product` (`productId`, `brandId`, `productName`, `productPrice`, `productStock`, `productRating`) VALUES
('PD102', 'BD001', 'STRIC 3', 20000000, 4, 8),
('PD067', 'BD001', 'SCAR 2', 25000000, 5, 10),
('PD107', 'BD001', 'A200', 12000000, 0, 8),
('PD695', 'BD002', 'B1200', 10000000, 1, 8);

-- --------------------------------------------------------

--
-- Struktur dari tabel `user`
--

CREATE TABLE `user` (
  `userId` varchar(10) NOT NULL,
  `userName` varchar(50) NOT NULL,
  `userEmail` varchar(50) NOT NULL,
  `userPassword` varchar(50) NOT NULL,
  `userGender` varchar(50) NOT NULL,
  `userAddress` varchar(50) NOT NULL,
  `userRole` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `user`
--

INSERT INTO `user` (`userId`, `userName`, `userEmail`, `userPassword`, `userGender`, `userAddress`, `userRole`) VALUES
('US130', 'member', 'member@gmail.com', 'member', 'Male', 'member Street', 0),
('US471', 'asdfdfsda', 'asdf@asdf.com', 'asdfss', 'Male', 'Street', 0),
('US744', 'testtest', 'test@gmail.com', 'test', 'Male', 'a Street', 1),
('US999', 'admin', 'admin@gmail.com', 'admin', 'Male', '-', 1);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
