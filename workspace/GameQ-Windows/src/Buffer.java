
public class Buffer {

	public int[] total;
	private int counter;
	private int size;
	public Buffer(int size) 
	{
		this.size = size;
		total = new int[size];
		counter = 0;
	}
	
	public void increment(int value) {
		total[counter] = value;
		if (counter == size-1) {
			counter = 0;
		} else {
			counter++;
		}
	}
	
	public int bufferValue() {
		int sum = 0;
		for (int i = 0; i < size; i++) {
			sum += total[i];
		}
		return sum;
	}
	
	
}
