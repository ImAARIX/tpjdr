# Generated by Selenium IDE
import pytest
import time
import json
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities

class TestTest4ouvertureduplateaudejeureussie:
  def setup_method(self, method):
    self.driver = webdriver.Chrome()
    self.vars = {}
  
  def teardown_method(self, method):
    self.driver.quit()
  
  def test_test4ouvertureduplateaudejeureussie(self):
    self.driver.get("http://localhost:5173/")
    self.driver.set_window_size(1227, 640)
    self.driver.find_element(By.CSS_SELECTOR, "input").click()
    self.driver.find_element(By.CSS_SELECTOR, "input").send_keys("Mon nom")
    self.driver.find_element(By.CSS_SELECTOR, "select").click()
    dropdown = self.driver.find_element(By.CSS_SELECTOR, "select")
    dropdown.find_element(By.XPATH, "//option[. = 'Warrior']").click()
    self.driver.find_element(By.CSS_SELECTOR, "button").click()

    # J'ai dû ajouter ici un timeout pour attendre le chargement de la page suivante
    # Ça n'avait pas été ajouté par l'IDE Selenium et le test bloquait ici
    WebDriverWait(self.driver, 10).until(
      expected_conditions.presence_of_element_located((By.ID, "plateauDeJeu"))
    )

    # Ici, les éléments de selenium natif
    # Redondant car vérifiés plus haut
    elements = self.driver.find_elements(By.ID, "plateauDeJeu")
    assert len(elements) > 0

    assert self.driver.find_element(By.CSS_SELECTOR, "h2").text == "Combat contre un monstre"
    assert self.driver.find_element(By.CSS_SELECTOR, "div:nth-child(2) > p:nth-child(2)").text == "Nom : Mon nom"
    assert self.driver.find_element(By.CSS_SELECTOR, "div:nth-child(2) > p:nth-child(3)").text == "Classe : WARRIOR"
  
