package ca.usask.cs.srlab.simcad.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.usask.cs.srlab.simcad.util.FastStringComparator;

/**
 * @author sharif
 *
 */
public abstract class CloneSet implements ICloneSet, Cloneable {
	
	public static final String CLONE_TYPE_1 = "Type-1";
	public static final String CLONE_TYPE_2 = "Type-2";
	public static final String CLONE_TYPE_3 = "Type-3";
	//public static final String CLONE_TYPE_4 = "Type-4";
	
	public static final String[] CLONE_TYPE_12 = new String[] {CLONE_TYPE_1, CLONE_TYPE_2};
	public static final String[] CLONE_TYPE_21 = CLONE_TYPE_12;
	public static final String[] CLONE_TYPE_23 = new String[] {CLONE_TYPE_2, CLONE_TYPE_3};
	public static final String[] CLONE_TYPE_32 = CLONE_TYPE_23;
	public static final String[] CLONE_TYPE_13 = new String[] {CLONE_TYPE_1, CLONE_TYPE_3};
	public static final String[] CLONE_TYPE_31 = CLONE_TYPE_13;
	public static final String[] CLONE_TYPE_NEARMISS = CLONE_TYPE_23;
	public static final String[] CLONE_TYPE_123 = new String[] {CLONE_TYPE_1, CLONE_TYPE_2, CLONE_TYPE_3};
	public static final String[] CLONE_TYPE_ALL = CLONE_TYPE_123;
	
	private Long id;
	private Integer cloneSetId;
	private List<ICloneFragment> cloneFragments;
	private String cloneType;	
	
	public CloneSet(CloneSet cloneSet) {
		this.id = cloneSet.id;
		this.cloneSetId = cloneSet.cloneSetId;
		this.cloneType = cloneSet.cloneType;
		this.cloneFragments = cloneSet.cloneFragments;
	}

	public CloneSet(Integer cloneSetId, List<ICloneFragment> cloneFragments,
			String cloneType) {
		super();
		this.cloneFragments = cloneFragments;
		this.cloneType = cloneType;
		this.cloneSetId = cloneSetId;
		
		CloneTypeMapper.mapTypeFor(this);
	}
	
	public Long getId() {
		return id;
	}
	
	public Integer getCloneSetId(){
		return cloneSetId;
	}
	
	public void setCloneSetId(int cloneSetId) {
		this.cloneSetId = cloneSetId;
	}
	
	@Override
	public List<ICloneFragment> getCloneFragments() {
		return cloneFragments;
	}

	public void setCloneFragments(List<? extends ICloneFragment> cloneFragments) {
		this.cloneFragments = (List<ICloneFragment>) cloneFragments;
	}
	
	@Override
	public String getCloneType() {
		return cloneType;
	}
	
	@Override
	public void setCloneType(String cloneType){
		this.cloneType = cloneType;
	}
	
	@Override
	public int size(){
		return cloneFragments.size();
	}

	@Override
	public ICloneFragment getMember(int i) {
		return cloneFragments.get(i);
	}

	private transient boolean subsumed;
	
	@Override
	public boolean isSubsumed() {
		return subsumed;
	}

	@Override
	public void setSubsumed(boolean subsumed) {
		this.subsumed = subsumed;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("clone "+ (this instanceof CloneGroup ? "group" : "pair") +": "+ cloneSetId+"\n");
		sb.append("number of fragments: "+ cloneFragments.size()+"\n");
		sb.append("clone type: "+ cloneType+"\n");
		for(ICloneFragment cf : cloneFragments){
			sb.append("\n");
			sb.append(cf.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

	
	public static class CloneTypeMapper {

		private CloneTypeMapper(){};
		
		public static void mapTypeFor(CloneSet cloneSet) {
			String cloneType = CLONE_TYPE_1;

			int type2_vote = 0;
			int type3_vote = 0;

			ICloneFragment cloneFragment_i = cloneSet.getMember(0);
	         
	        for (int j = 1; j < cloneSet.size(); j++) {
	        	ICloneFragment cloneFragment_j = cloneSet.getMember(j);
	        	 
	        	if (cloneFragment_i.getLineOfCode() != cloneFragment_j.getLineOfCode()
	        			|| !cloneFragment_i.getSimhash1().equals(cloneFragment_j.getSimhash1())) {
	        		 //if difference in line or hash code (note: line might be same in case of type 3 where a line has been replaced by another)
	        		cloneType = CLONE_TYPE_3;
	        		type3_vote++;
	                break;
	            }else {
	            	//either type 1 (equal simhash and equal original source) or 2 (equal simhash but dissimilar original source)
	            	if (cloneFragment_i.getSimhash1().equals(cloneFragment_j.getSimhash1())
	            			&& FastStringComparator.INSTANCE.compare(cloneFragment_i.getOriginalCodeBlock(), cloneFragment_j.getOriginalCodeBlock()) != 0) {
	            		type2_vote++;
	            	}
	            }
	         } //for
	         
	         if (type3_vote == 0 && type2_vote > 0) {
	             cloneType = CLONE_TYPE_2;
	         }
	         
	         cloneSet.setCloneType(cloneType);
		}

		public static String[] getTypeFromString(String typeString) {
			Set<String> types = new HashSet<String>(3);
			if(typeString.contains("1"))
				types.add(CLONE_TYPE_1);
			if(typeString.contains("2"))
				types.add(CLONE_TYPE_2);
			if(typeString.contains("3"))
				types.add(CLONE_TYPE_3);
			if(typeString.equals("nearmiss"))
				types.addAll(Arrays.asList(CLONE_TYPE_NEARMISS));
			if(typeString.equals("all"))
				types.addAll(Arrays.asList(CLONE_TYPE_ALL));
			return types.toArray(new String[0]);
		}
		
		public static String getTypeStringFromCollection(Collection<String> cloneTypes){
			String typeString = null;
			if(cloneTypes.contains(CLONE_TYPE_1))
				typeString = "1";
			if(cloneTypes.contains(CLONE_TYPE_2)){
				if(typeString == null)
					typeString = "2";
				else
					typeString = typeString+"2";
			}
			if(cloneTypes.contains(CLONE_TYPE_3))
				if(typeString == null)
					typeString = "3";
				else
					typeString = typeString+"3";
			return typeString;
		}
		
		public static String getTypeStringFromArray(String cloneTypes[]){
			String typeString = null;
			Collection<String> cloneTypeList = Arrays.asList(cloneTypes);
			
			if(cloneTypeList.contains(CLONE_TYPE_1))
				typeString = "1";
			if(cloneTypeList.contains(CLONE_TYPE_2)){
				if(typeString == null)
					typeString = "2";
				else
					typeString = typeString+"2";
			}
			if(cloneTypeList.contains(CLONE_TYPE_3))
				if(typeString == null)
					typeString = "3";
				else
					typeString = typeString+"3";
			return typeString;
		}
	}
	
	@Override
	public CloneSet clone() throws CloneNotSupportedException {
		CloneSet clone = (CloneSet) super.clone();
		List<ICloneFragment> cf = new ArrayList<ICloneFragment>(cloneFragments.size());
		cf.addAll(cloneFragments);
		clone.cloneFragments = cf;
		return clone;
	}
	
}
