import java.util.*;
import java.io.*;

public class jOSproj1 {
	//Simulation Environment Variables
	public static final int AVG_IO_TIME = 400;
	public static final int IO_BURST_LENGTH = 100;
	public static final int BURST_LENGTH = 66;

	//Memory structures
	public static final int CURRENT_PCB = 1;
	public static final int OLD_PCB = 2;
	public static final int STACK_PTR = 3;
	public static final int INTERRUPT_V_START = 9;
	public static final int INTERRUPT_V_END = 23;
	public static final int PMT_START = 24;
	public static final int LOADED_PROC_START = 115;
	public static final int READY_Q_START = 140;
	public static final int PSW = 291;
	public static final int WAIT_Q_START = 300;
	public static final int FSB_START = 800;
	
	//memory structure properties
	public static final int WORKSPACE_SIZE = 20;
	public static final int NEXT_AVAIL_ADDR = 0;
	public static final int PMT_UNIT_SIZE = 3;
	public static final int PCB_LENGTH = 5;
	public static final int PID = 0;
	public static final int TIME_REQUIRED = 1;
	public static final int IO_BOOL = 2;
	public static final int ELAPSED_TIME = 3;
	public static final int ELAPSED_IO = 4;
	public static final int VALUE = 0;
	public static final int NEXT_WORD = 1;
	public static final int PREV_WORD = 2;
	public static final int FIRST = -1;
	public static final int LAST = -2;
	public static final int NULL = -3;
	public static final int NOT_FOUND = -4;
	public static final int BEG_ADDRESS = 1;
	public static final int ADDR_RANGE = 2;
	public static int[][] memory = new int[10000][4];
	public static int[][] jobTable = new int[30][3];
	public static int jobsCompleted = 0;
	public static int jobsEntered = 0;
	public static int jobsNum = 0;

	//DONE
	public static int readFile(int jobNum, char op) {
		File file = new File("jobs.txt");
		String line = new String();
		String search = new String();
		int timeRequired, ioBool = 0;
		try {
			Scanner jobs = new Scanner(file);
			jobs.useDelimiter(",|\\n");
			while(jobs.hasNextLine()) {
				line = jobs.nextLine();
				search = line;
				search = search.substring(0, search.indexOf(','));
				if(jobNum == Integer.valueOf(search)) {
					break;
				}
			}
		}
		catch (IOException ex) {
			System.out.println("Input file could not be found.");
		}
		switch(op) {
			case 'r':
				return Integer.valueOf(line.substring(line.indexOf(',') + 1, line.lastIndexOf(',')));
			case 'i':
				return Integer.valueOf(line.substring(line.lastIndexOf(',') + 1));
			default:
				return -1; 
		}
	}

	//DONE
	public static int jobCount() {
		File file = new File("jobs.txt");
		int jobNum = 0;
		String line = new String();
		try {
			Scanner jobs = new Scanner(file);
			jobs.useDelimiter(",|\\n");
			while(jobs.hasNextLine()) {
				line = jobs.nextLine();
				jobNum += 1;
			}
		}
		catch (IOException ex) {
			System.out.println("Input file could not be found to count jobs.");
			System.out.println("roggOS will now exit.");
		}
		return jobNum;
	}

	//DONE
	public static int randGen(int jobNum) {
		Random rand = new Random();
		int number = rand.nextInt(jobNum);
		System.out.println("==RandNum--");
		System.out.println(number);
		return number;
	}

	public static void createPCB(int pid, int timeRequired, int ioBool, int startingAddress) {
		memory[startingAddress + PID][VALUE] = pid;
		memory[startingAddress + PID][NEXT_WORD] = startingAddress + TIME_REQUIRED; 
		memory[startingAddress + PID][PREV_WORD] = FIRST;
		memory[startingAddress + TIME_REQUIRED][VALUE] = timeRequired;
		memory[startingAddress + TIME_REQUIRED][NEXT_WORD] = startingAddress + IO_BOOL;
		memory[startingAddress + TIME_REQUIRED][PREV_WORD] = startingAddress;
		memory[startingAddress + IO_BOOL][VALUE] = ioBool;
		memory[startingAddress + IO_BOOL][NEXT_WORD] = startingAddress + ELAPSED_TIME;
		memory[startingAddress + IO_BOOL][PREV_WORD]  = startingAddress + TIME_REQUIRED;
		memory[startingAddress + ELAPSED_TIME][VALUE] = 0;
		memory[startingAddress + ELAPSED_TIME][NEXT_WORD] = startingAddress + ELAPSED_IO;
		memory[startingAddress + ELAPSED_TIME][PREV_WORD] = startingAddress + IO_BOOL;
		memory[startingAddress + ELAPSED_IO][VALUE] = 0;
		memory[startingAddress + ELAPSED_IO][NEXT_WORD] = startingAddress + ELAPSED_IO + 1;
		memory[startingAddress + ELAPSED_IO][PREV_WORD] = startingAddress + ELAPSED_IO;
		memory[startingAddress + ELAPSED_IO + 1][NEXT_WORD] = LAST;
	}

	public static void shuffleQueue(int position) {
		for(int i = position; i < jobCount(); i++) {
			try {			
				jobTable[i][PID] = jobTable[i + 1][PID];
				jobTable[i][TIME_REQUIRED] = jobTable[i + 1][TIME_REQUIRED];
				jobTable[i][IO_BOOL] = jobTable[i + 1][IO_BOOL];
			}
			catch(IndexOutOfBoundsException outBound) {
				break;
			}
		}
	}

	public static void loadToJobTable(int pid, int timeReq, int ioBoolean, int position) {
		jobTable[position][PID] = pid;
		jobTable[position][TIME_REQUIRED] = timeReq;
		jobTable[position][IO_BOOL] = ioBoolean;
	}

	public static void newLoadToReadyQ() {
		int position, timeRequired, ioBool, startingAddress = 0;
		for(int i = 0; i < jobCount(); i++) {
			position = randGen(jobCount());
			
	}

	public static void loadToReadyQ() {
		int pid, timeRequired, ioBool, startingAddress = 0;
		for(int i = 0; i < jobCount(); i++) {
			pid = randGen(jobCount());
			if(isJobAlreadyLoaded(pid) == true) {
				while(isJobAlreadyLoaded(pid)) {
					pid = randGen(jobCount());
				}
			}
			System.out.println("\npid = " + pid);
			addToAlreadyLoaded(pid);
			updatePMT(pid, NULL, NULL);
			timeRequired = readFile(pid, 'r');
			ioBool = readFile(pid, 'i');       
			createPCB(pid, timeRequired, ioBool, lastElementOf(READY_Q_START, WAIT_Q_START));
		}
	}

	//DONE
	public static void initStructure(int startAddr, int size) {
		for(int i = startAddr; i < startAddr + size; i++) {
			memory[i][VALUE] = NULL;
			memory[i][NEXT_WORD] = i + 1;
			if(i == startAddr + size - 1) {
				memory[i][NEXT_WORD] = LAST;
			}
			memory[i][PREV_WORD] = i - 1;
			if(i == startAddr) {
				memory[i][PREV_WORD] = FIRST;
			}
		}
	}

	public static void initSystem() {
		System.out.print("Initializing...");
		memory[CURRENT_PCB][VALUE] = NULL;
		memory[OLD_PCB][VALUE] = NULL;
		memory[LOADED_PROC_START][NEXT_WORD] = LAST;
		memory[LOADED_PROC_START][PREV_WORD] = FIRST;
		memory[LOADED_PROC_START][VALUE] = NULL;
		initStructure(READY_Q_START, 1);
		initStructure(PSW, 5);
		memory[PMT_START][VALUE] = NULL;
		memory[PMT_START][NEXT_WORD] = LAST;
		memory[PMT_START][PREV_WORD] = FIRST;
		System.out.print("done.\n");
	}

	//DONE
	public static boolean isJobAlreadyLoaded(int job) {
		int i = LOADED_PROC_START;
		while(i < PSW) {
			if(memory[i][VALUE] == job) {
				return true;
			}
			i++;
		}
		return false;
	}

	//DONE
	public static int lastElementOf(int startAddr, int nextMemoryStructure) {
		while(startAddr < nextMemoryStructure) {
			if(memory[startAddr][NEXT_WORD] == LAST) {
				return startAddr;
			}
			startAddr++;
		}
		return NOT_FOUND;
	}

	//DONE
	public static void addToAlreadyLoaded(int job) {
		System.out.println("addToAlreadyLoaded ------");
		if(memory[LOADED_PROC_START][NEXT_WORD] == LAST) {
			System.out.println("AlreadyLoaded is empty");
			memory[LOADED_PROC_START][VALUE] = job;
			memory[LOADED_PROC_START][NEXT_WORD] = LOADED_PROC_START + 1;
			memory[LOADED_PROC_START + 1][NEXT_WORD] = LAST;
			System.out.println("addr " + LOADED_PROC_START);
			System.out.println("Val " + memory[LOADED_PROC_START][VALUE]);
			System.out.println("Next " + memory[LOADED_PROC_START][NEXT_WORD]);
			System.out.println("Next one's Next " + memory[LOADED_PROC_START + 1][NEXT_WORD]);
		}
		else {
			System.out.println("AlreadyLoaded is not empty");
			System.out.println("Addr " + (lastElementOf(LOADED_PROC_START, PSW)));
			memory[lastElementOf(LOADED_PROC_START, PSW)][VALUE] = job;
			System.out.println("Val " + memory[lastElementOf(LOADED_PROC_START, PSW)][VALUE]);
			memory[lastElementOf(LOADED_PROC_START, PSW)][PREV_WORD] = lastElementOf(LOADED_PROC_START, PSW) - 1;
			System.out.println("Prev " + memory[lastElementOf(LOADED_PROC_START, PSW)][PREV_WORD]);
			System.out.println("lastElement " + lastElementOf(LOADED_PROC_START, PSW)); //33
			memory[lastElementOf(LOADED_PROC_START, PSW) + 1][NEXT_WORD] = LAST;
			memory[lastElementOf(LOADED_PROC_START, PSW)][NEXT_WORD] = lastElementOf(LOADED_PROC_START, PSW) + 1;
			System.out.println("lastElement " + lastElementOf(LOADED_PROC_START, PSW));
			System.out.println("Next " + memory[lastElementOf(LOADED_PROC_START, PSW)][NEXT_WORD]);
		}
	}

	//DONE
	public static void listMemoryStructure(int startAddr) {
		while(memory[startAddr][NEXT_WORD] != LAST) {
			System.out.println("--Address " + startAddr);
			System.out.println("Val " + memory[startAddr][VALUE]);
			System.out.println("Next " + memory[startAddr][NEXT_WORD]);
			System.out.println("Prev " + memory[startAddr][PREV_WORD]);
			startAddr++;
			if(memory[startAddr][NEXT_WORD] == LAST) {
				System.out.println("--Address " + startAddr);
				System.out.println("Val " + memory[startAddr][VALUE]);
				System.out.println("Next " + memory[startAddr][NEXT_WORD]);
				System.out.println("Prev " + memory[startAddr][PREV_WORD]);
			}
		}
	}
	//DONE
	public static void updatePMT(int pid, int addrRange, int begAddress) {
		int next = lastElementOf(PMT_START, READY_Q_START);
		System.out.println("Update PMT ---------");

		if(memory[PMT_START][NEXT_WORD] == LAST) {
			System.out.println("PMT is empty");
			memory[PMT_START][VALUE] = pid;
			memory[PMT_START][NEXT_WORD] = PMT_START + BEG_ADDRESS;
			memory[PMT_START + BEG_ADDRESS][VALUE] = begAddress;
			memory[PMT_START + BEG_ADDRESS][NEXT_WORD] = PMT_START + ADDR_RANGE;
			memory[PMT_START + BEG_ADDRESS][PREV_WORD] = PMT_START;
			memory[PMT_START + ADDR_RANGE][VALUE] = addrRange;
			memory[PMT_START + ADDR_RANGE][NEXT_WORD] = PMT_START + ADDR_RANGE + 1;
			memory[PMT_START + ADDR_RANGE][PREV_WORD] = PMT_START + BEG_ADDRESS;
			memory[PMT_START + ADDR_RANGE + 1][NEXT_WORD] = LAST;
			System.out.println("- addr" + " " + PMT_START);
			System.out.println("pid" + " " + memory[PMT_START][VALUE]);
			System.out.println("Next" + " " + memory[PMT_START][NEXT_WORD]);
			System.out.println("- addr " + (PMT_START + BEG_ADDRESS));
			System.out.println("addr range " + memory[PMT_START + BEG_ADDRESS][VALUE]);
			System.out.println("Next " + memory[PMT_START + BEG_ADDRESS][NEXT_WORD]);
			System.out.println("- addr " + (PMT_START + ADDR_RANGE));
			System.out.println("beg addr " + memory[PMT_START + ADDR_RANGE][VALUE]);
			System.out.println("Next " + memory[PMT_START + ADDR_RANGE][NEXT_WORD]);
		}
		else {
			System.out.println("PMT is not empty");
			
			memory[next][VALUE] = pid;
			memory[next][NEXT_WORD] = lastElementOf(PMT_START, READY_Q_START) + BEG_ADDRESS;
			memory[next + BEG_ADDRESS][VALUE] = begAddress;
			memory[next + BEG_ADDRESS][NEXT_WORD] = next + ADDR_RANGE;
			memory[next + BEG_ADDRESS][PREV_WORD] = next;
			memory[next + ADDR_RANGE][VALUE] = addrRange;
			memory[next + ADDR_RANGE][NEXT_WORD] = next + ADDR_RANGE + 1;
			memory[next + ADDR_RANGE][PREV_WORD] = next + BEG_ADDRESS;
			memory[next + ADDR_RANGE + 1][NEXT_WORD] = LAST;
	
			System.out.println("- addr " + next);
			System.out.println("pid " + memory[next][VALUE]);
			System.out.println("Next " + memory[next][NEXT_WORD]);
			System.out.println("- addr " + (next + BEG_ADDRESS));
			System.out.println("beg addr " + memory[next + BEG_ADDRESS][VALUE]);
			System.out.println("Next " + memory[next + BEG_ADDRESS][NEXT_WORD]);
			System.out.println("- addr " + (next + ADDR_RANGE));
			System.out.println("addr range" + memory[next + ADDR_RANGE][VALUE]);
			System.out.println("Next " + memory[next + ADDR_RANGE][NEXT_WORD]);
		}
	}
	//DONE
	//Determines the size of a memory structure. The value returned is subtracted 1 because the structures
	//set the LAST value at not the actual last entry itself but rather the element following the last
	//entry.
	public static int memStructLength(int queueStart, int elementSize) {
		int counter = 0;
		int i = queueStart;
		while(memory[i][NEXT_WORD] != LAST) {
				if((i - queueStart >= 0) && ((i - queueStart) % elementSize == 0)) {
				counter++;
			}
			else if(memory[i][NEXT_WORD] == LAST) {
				break;
			}
			i++;
		}
		return counter -1;
	}

	//public static int getNextProcess() {}

	public static void main(String[] args) {
		int job = 0;
		System.out.println("--- roggOS --------------------------------\n");
		initSystem();
		loadToReadyQ();
		System.out.println("\nList PMT ---");
		listMemoryStructure(PMT_START);
		//System.out.println("length: \n" + memStructLength(PMT_START, 3));
	}
}
