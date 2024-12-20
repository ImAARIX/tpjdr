up:
	docker compose up -d --build

down:
	docker compose down --remove-orphans

testJava:
	./back/gradlew -p ./back test

testJs:
	npm --prefix ./front test

testSelenium:
	python3 -m unittest discover -s ./python-selenium/tests/
