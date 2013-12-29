package GUI;

import java.util.LinkedList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import POJOs.Car;

/**
 * Contains static methods to refresh tables models in GUI
 * @author Sabina Rydzek, Kacper Furmañski, Mateusz Kotlarz
 *
 */
public class TablesRefresher {
	
	/**
	 * Refresh Drivers Table Model
	 * @param board
	 * @param table
	 */
	public static void refreshDriversTableModel(Board board, JTable table)
	{
		DefaultTableModel defmodel = new DefaultTableModel(null, new String[] {"No. ", "Name", "Skill", "Lap", "Speed", "Accelerate", "KERS %"}); 
		LinkedList<Car> cars = board.getCars();
		for(Car car : cars)
		{
			Object[] data = new Object[7];
			data[0] = car.getNumber();
			data[1] = car.getDriverName();
			data[2] = car.getDriverSkills(); 
			data[3] = car.getLaps();
			data[4] = Math.round(car.getSpeed())+" km/h";
			data[5] = ((double)(Math.round(car.getAcceleration()*1000))/1000)+" m/s2";
			data[6] = car.getKersSystemPercent()+" %";
			defmodel.addRow(data);
		}
        table.setModel(defmodel);
        table.getColumnModel().getColumn(0).setPreferredWidth(27);
		table.getColumnModel().getColumn(3).setPreferredWidth(27);
		table.getColumnModel().getColumn(4).setPreferredWidth(40);
		table.getColumnModel().getColumn(5).setPreferredWidth(55);
		table.getColumnModel().getColumn(6).setPreferredWidth(35);
		table.repaint();
	}
	
	/**
	 * Create Result Table Model
	 * @param board
	 * @param tableResult
	 */
	public static void createResultTableModel(Board board, JTable tableResult)
	{
		DefaultTableModel defmodel = new DefaultTableModel(null, new String[] {"Pos.", "Name", "Skill", "Lap", "Actual Lap Time", "Best Lap Time"}); 
		LinkedList<Car> cars = board.getCars();
		int i=0;
		for(Car car : cars)
		{
			Object[] data = new Object[6];
			data[0] = i+1;
			data[1] = car.getDriverName();
			data[2] = car.getDriverSkills(); 
			data[3] = car.getLaps();
			data[4] = car.getActualLapTime();
			if(car.getBestLapTime() != 1000000) data[5] = car.getBestLapTime();
			else data[5] = "-";
			defmodel.addRow(data);
			i++;
		}
		
		tableResult.setModel(defmodel);
        tableResult.getColumnModel().getColumn(0).setPreferredWidth(27);
		tableResult.getColumnModel().getColumn(3).setPreferredWidth(27);
		tableResult.getColumnModel().getColumn(4).setPreferredWidth(75);
		tableResult.getColumnModel().getColumn(5).setPreferredWidth(75);
		tableResult.repaint();
	}
	
	/**
	 * Refresh Results Table Model
	 * @param board
	 * @param tableResult
	 */
	public static void refreshResultTableModel(Board board, JTable tableResult)
	{
		TableModel oldModel = tableResult.getModel();
		DefaultTableModel defmodel = new DefaultTableModel(null, new String[] {"Pos.", "Name", "Skill", "Lap", "Actual Lap Time", "Best Lap Time"}); 
		LinkedList<Car> cars = board.getCars();
		if(oldModel.getColumnCount() == 0) //First Time
		{
			int i=0;
			for(Car car : cars)
			{
				Object[] data = new Object[6];
				data[0] = i+1;
				data[1] = car.getDriverName();
				data[2] = car.getDriverSkills(); 
				data[3] = car.getLaps();
				data[4] = car.getActualLapTime();
				data[5] = "-";
				defmodel.addRow(data);
				i++;
			}
		}
		else
		{
			//Copy old model to objects 2d array
			Object[][] data = new Object[oldModel.getRowCount()][6];
			for(int i=0; i<oldModel.getRowCount(); i++)
				for(int j=0; j<6; j++) 
					data[i][j] = oldModel.getValueAt(i, j);

			//Check outed cars
			for(int i=0; i<data.length; i++)
			{
				int j=0;
				while(j != cars.size() && !cars.get(j).getDriverName().equals(data[i][1].toString())) j++;
				if(j == cars.size()) data[i][0] = "OUT";
			}
			
			//Actualize array
			for(Car car : cars)
			{
				int i=0;
				while(!car.getDriverName().equals(data[i][1].toString())) i++;
				data[i][3] = car.getLaps();
				
				//Formatting actual time
				long actTime = car.getActualLapTime();
				String s,ms;
				s = (actTime/1000)%60+"";
				if(s.length() == 1) s = "0"+s;
				ms = actTime%1000+"";
				if(ms.length() == 1) ms = "00"+ms;
				else if(ms.length() == 2) ms = "0"+ms;
				
				data[i][4] = (actTime/60000)+":"+s+"."+ms;
				if(car.getBestLapTime() != 1000000) 
				{
					//Formatting best time
					long bestTime = car.getBestLapTime();
					s = (bestTime/1000)%60+"";
					if(s.length() == 1) s = "0"+s;
					ms = bestTime%1000+"";
					if(ms.length() == 1) ms = "00"+ms;
					else if(ms.length() == 2) ms = "0"+ms;
					
					data[i][5] = (bestTime/60000)+":"+s+"."+ms;
				}
				else data[i][5] = "-";
				while(i > 0 && (data[i-1][0].toString().equals("OUT") || (int) data[i][3] > (int) data[i-1][3]))
				{
					data[i][0] = i;
					if(!data[i-1][0].toString().equals("OUT")) data[i-1][0] = i+1;
					Object[] temp = data[i];
					data[i] = data[i-1];
					data[i-1] = temp;
					i--;
				}
			}
			
			//Copy array to TableModel
			for(int i=0; i<data.length; i++)
				defmodel.addRow(data[i]);
		}
		
		tableResult.setModel(defmodel);
        tableResult.getColumnModel().getColumn(0).setPreferredWidth(27);
		tableResult.getColumnModel().getColumn(3).setPreferredWidth(27);
		tableResult.getColumnModel().getColumn(4).setPreferredWidth(75);
		tableResult.getColumnModel().getColumn(5).setPreferredWidth(75);
		tableResult.repaint();
	}
}
