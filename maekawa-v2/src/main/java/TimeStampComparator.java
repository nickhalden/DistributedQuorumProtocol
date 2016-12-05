import java.util.Comparator;

public class TimeStampComparator implements Comparator<NodeInfo> {

	public int compare(NodeInfo o1, NodeInfo o2) {
		// TODO Auto-generated method stub
		if(o1.getTimestamp()-o2.getTimestamp()>0)
			return 1;
		return -1;

	}

}
