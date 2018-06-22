-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema f1
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema f1
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `f1` DEFAULT CHARACTER SET latin1 ;
USE `f1` ;

-- -----------------------------------------------------
-- Table `f1`.`escuderies`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `f1`.`escuderies` (
  `nom` VARCHAR(45) NOT NULL,
  `pais` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`nom`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `f1`.`bolids`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `f1`.`bolids` (
  `num_bolid` INT(11) NOT NULL,
  `nom_dissenyador` VARCHAR(45) NOT NULL,
  `escuderia_nom` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`num_bolid`),
  INDEX `FK_EscuderiaBolid` (`escuderia_nom` ASC),
  CONSTRAINT `FK_EscuderiaBolid`
    FOREIGN KEY (`escuderia_nom`)
    REFERENCES `f1`.`escuderies` (`nom`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `f1`.`circuits`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `f1`.`circuits` (
  `nom` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`nom`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `f1`.`treballadors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `f1`.`treballadors` (
  `nom` VARCHAR(45) NOT NULL,
  `escuderia_nom` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`nom`),
  INDEX `FK_EscuderiaTreballador_idx` (`escuderia_nom` ASC),
  CONSTRAINT `FK_EscuderiaTreballador`
    FOREIGN KEY (`escuderia_nom`)
    REFERENCES `f1`.`escuderies` (`nom`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `f1`.`contractes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `f1`.`contractes` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `data_inici` DATE NOT NULL,
  `data_fi` DATE NULL DEFAULT NULL,
  `escuderia_nom` VARCHAR(45) NOT NULL,
  `treballador_nom` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_EscuderiaContracte_idx` (`escuderia_nom` ASC),
  INDEX `FK_TreballadorContracte_idx` (`treballador_nom` ASC),
  CONSTRAINT `FK_EscuderiaContracte`
    FOREIGN KEY (`escuderia_nom`)
    REFERENCES `f1`.`escuderies` (`nom`),
  CONSTRAINT `FK_TreballadorContracte`
    FOREIGN KEY (`treballador_nom`)
    REFERENCES `f1`.`treballadors` (`nom`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `f1`.`mecanics`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `f1`.`mecanics` (
  `nom` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`nom`),
  CONSTRAINT `FK_TreballadorMecanic`
    FOREIGN KEY (`nom`)
    REFERENCES `f1`.`treballadors` (`nom`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `f1`.`parts_bolid`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `f1`.`parts_bolid` (
  `codi` INT(11) NOT NULL,
  `descripcio` VARCHAR(45) NOT NULL,
  `bolid_num` INT(11) NOT NULL,
  `mecanic_nom` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`codi`),
  INDEX `FK_BolidPart` (`bolid_num` ASC),
  INDEX `FK_MecanicPart_idx` (`mecanic_nom` ASC),
  CONSTRAINT `FK_BolidPart`
    FOREIGN KEY (`bolid_num`)
    REFERENCES `f1`.`bolids` (`num_bolid`),
  CONSTRAINT `FK_MecanicPart`
    FOREIGN KEY (`mecanic_nom`)
    REFERENCES `f1`.`mecanics` (`nom`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `f1`.`pilots`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `f1`.`pilots` (
  `nom` VARCHAR(45) NOT NULL,
  `sou` FLOAT NOT NULL,
  PRIMARY KEY (`nom`),
  CONSTRAINT `FK_TreballadorPilot`
    FOREIGN KEY (`nom`)
    REFERENCES `f1`.`treballadors` (`nom`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `f1`.`provadors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `f1`.`provadors` (
  `nom` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`nom`),
  CONSTRAINT `FK_PilotProvador`
    FOREIGN KEY (`nom`)
    REFERENCES `f1`.`pilots` (`nom`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `f1`.`valoracions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `f1`.`valoracions` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `valoracio` INT(11) NOT NULL,
  `provador_nom` VARCHAR(45) NOT NULL,
  `bolid_num` INT(11) NOT NULL,
  `circuit_nom` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_BolidValoracio_idx` (`provador_nom` ASC),
  INDEX `FK_BolidValoracio_idx1` (`bolid_num` ASC),
  INDEX `FK_CircuitValoracio_idx` (`circuit_nom` ASC),
  CONSTRAINT `FK_BolidValoracio`
    FOREIGN KEY (`bolid_num`)
    REFERENCES `f1`.`bolids` (`num_bolid`),
  CONSTRAINT `FK_CircuitValoracio`
    FOREIGN KEY (`circuit_nom`)
    REFERENCES `f1`.`circuits` (`nom`),
  CONSTRAINT `FK_ProvadorValoracio`
    FOREIGN KEY (`provador_nom`)
    REFERENCES `f1`.`provadors` (`nom`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
