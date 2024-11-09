package project.elevator;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ElevatorSystemMonitor {
	private ElevatorSubsystem elevatorSystem;

	private JFrame frame1;
	private JFrame frame2;

	private TableModel elevatorFloorsModel;
	private JTable elevatorFloorsTable;

	private TableModel elevatorDestinationsModel;
	private JTable elevatorDestinationsTable;

	public ElevatorSystemMonitor(ElevatorSubsystem elevatorSystem) {
		this.elevatorSystem = elevatorSystem;

		initializeTableModel();

		frame1 = new JFrame();
		frame1.setTitle("Elevator Floors");

		elevatorFloorsTable = new JTable(elevatorFloorsModel);
		elevatorFloorsTable.setBounds(20, 20, 20, 20);
		var pane1 = new JScrollPane(elevatorFloorsTable);

		frame1.add(pane1);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setSize(900, 500);
		frame1.setVisible(true);

		frame2 = new JFrame();
		frame2.setTitle("Elevator Destinations");

		elevatorDestinationsTable = new JTable(elevatorDestinationsModel);
		elevatorDestinationsTable.setBounds(20, 20, 20, 20);
		var pane2 = new JScrollPane(elevatorDestinationsTable);

		frame2.add(pane2);
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.setSize(400, 600);
		frame2.setVisible(true);
	}

	private void initializeTableModel() {
		elevatorFloorsModel = new AbstractTableModel() {
			@Override
			public int getColumnCount() { return ElevatorSubsystem.NUMBER_OF_ELEVATORS + 1; }

			@Override
			public int getRowCount() { return ElevatorSubsystem.NUMBER_OF_FLOORS + 1; }

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				var floor = ElevatorSubsystem.NUMBER_OF_FLOORS - rowIndex + 1;
				if (rowIndex == 0) {
					return columnIndex == 0
						? "Floors"
						: elevatorSystem.getElevators().get(columnIndex - 1);
				} else if (columnIndex == 0) {
					return "Floor " + floor;
				} else {
					var elevator = elevatorSystem.getElevators().get(columnIndex - 1);
					return elevator.currentFloor() == floor
						? elevator.currentState()
						: null;
				}
			}
		};

		elevatorDestinationsModel = new AbstractTableModel() {
			@Override
			public int getColumnCount() { return ElevatorSubsystem.NUMBER_OF_ELEVATORS + 1; }

			@Override
			public int getRowCount() { return ElevatorSubsystem.NUMBER_OF_FLOORS + 1; }

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				var floor = rowIndex;
				if (columnIndex == 0) {
					return rowIndex == 0
						? "Floors"
						: floor;
				} else if (rowIndex == 0) {
					return elevatorSystem.getElevators().get(columnIndex - 1);
				} else {
					var elevator = elevatorSystem.getElevators().get(columnIndex - 1);
					return elevator.containsDestination(floor)
						? "•"
						: null;
				}
			}
		};
	}

	public void update() {
		elevatorFloorsTable.updateUI();
		elevatorDestinationsTable.updateUI();
	}
}
