print "Lancement d'un tank"

tank.moveForward()

	frontInfo = tank.getFrontInformations();
	rightInfo = tank.getRightInformations();
	behindInfo = tank.getBehindInformations();
	leftInfo = tank.getLeftInformations();
while True:
	if rightInfo.getType() == tank.typeFromString('TANK'):
		tank.turnCanonClockwise();
		tank.shoot();
		tank.turnCanonCounterClockwise();

	if leftInfo.getType() == tank.typeFromString('TANK'):
		tank.turnCanonCounterClockwise();
		tank.shoot();
		tank.turnCanonClockwise();

	if behindInfo.getType() == tank.typeFromString('TANK'):
		tank.turnCanonCounterClockwise();
		tank.turnCanonCounterClockwise();
		tank.shoot();
		tank.turnCanonCounterClockwise();
		tank.turnCanonCounterClockwise();

	while frontInfo.getDistance() > 0 and frontInfo.getDistance() < 3 and frontInfo.getType() == tank.typeFromString('WALL'):
		tank.turnCounterClockwise();
		frontInfo = tank.getFrontInformations();
		rightInfo = tank.getRightInformations();
		behindInfo = tank.getBehindInformations();
		leftInfo = tank.getLeftInformations();

	tank.moveForward();
