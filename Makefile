run: clear
	cd ~/dev/node_ws/event_sourcing_poc;docker-compose build
	cd ~/dev/node_ws/event_sourcing_poc;docker-compose up
	${MAKE} clear

clear:
	cd ~/dev/node_ws/event_sourcing_poc;docker-compose kill
	cd ~/dev/node_ws/event_sourcing_poc;docker-compose rm -fv
