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

class TestTest2pasdenomdanslinput:
  def setup_method(self, method):
    self.driver = webdriver.Chrome()
    self.vars = {}
  
  def teardown_method(self, method):
    self.driver.quit()
  
  def test_test2pasdenomdanslinput(self):
    self.driver.get("http://localhost:5173/")
    self.driver.set_window_size(1227, 640)
    self.driver.find_element(By.CSS_SELECTOR, "input").click()
    self.driver.find_element(By.CSS_SELECTOR, "input").send_keys("Mon nom")
    self.driver.find_element(By.CSS_SELECTOR, "button").click()
    assert self.driver.switch_to.alert.text == "Veuillez entrer un nom et choisir une classe !"
    self.driver.switch_to.alert.accept()
    elements = self.driver.find_elements(By.ID, "plateauDeJeu")
    assert len(elements) == 0
  