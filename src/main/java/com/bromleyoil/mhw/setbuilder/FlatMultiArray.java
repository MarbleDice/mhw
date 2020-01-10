package com.bromleyoil.mhw.setbuilder;

import java.util.List;

/**
 * Models a multi-dimensional array of records, grouped by category. Each record has one or more fields. Internally
 * implemented as a single-dimensional array for parallel performance.
 * 
 * @author moorwi
 *
 */
class FlatMultiArray {

	private byte[] data;
	private int numCategories;
	private int recordLength;
	private int[] categoryLength;
	private int[] categoryOffset;

	/**
	 * Initializes a new FlatMultiArray of the given size.
	 * 
	 * @param numCategories
	 *     The number of categories.
	 * @param numRecords
	 *     The total number of records in all categories.
	 * @param numFields
	 *     The number of fields in each record.
	 */
	public FlatMultiArray(int numCategories, int numRecords, int numFields) {
		this.numCategories = numCategories;
		recordLength = numFields;
		data = new byte[numRecords * recordLength];
		categoryLength = new int[numCategories];
		categoryOffset = new int[numCategories];
	}

	public static byte[] convertToByteArray(List<Integer> list) {
		byte[] rv = new byte[list.size()];
		for (int i = 0; i < list.size(); i++) {
			rv[i] = (byte) list.get(i).intValue();
		}
		return rv;
	}

	/**
	 * Adds an entire category of records at once. Values are assumed to be in field order.
	 * 
	 * @param category
	 * @param values
	 */
	public void addCategory(int category, List<Integer> values) {
		for (int i = 0; i < values.size(); i++) {
			data[categoryOffset[category] + i] = (byte) values.get(i).intValue();
		}

		categoryLength[category] = values.size() / recordLength;
		if (category < numCategories - 1) {
			categoryOffset[category + 1] = categoryOffset[category] + values.size();
		}
	}

	/**
	 * Returns a single value for the given field in the given record in the given category.
	 * 
	 * @param category
	 * @param record
	 * @param field
	 * @return
	 */
	public byte getField(int category, int record, int field) {
		return data[categoryOffset[category] + record * recordLength + field];
	}

	/**
	 * Returns an array of indexes for every category, corresponding to the given permutation number.
	 * 
	 * @param perm
	 * @return
	 */
	public int[] getIndexesForPerm(int perm) {
		int[] indexByType = new int[numCategories];

		int permProduct = 1;
		for (int i = 0; i < numCategories; i++) {
			indexByType[i] = perm / permProduct % categoryLength[i];
			permProduct *= categoryLength[i];
		}

		return indexByType;
	}

	public int getNumCategories() {
		return numCategories;
	}

	public void setNumCategories(int numCategories) {
		this.numCategories = numCategories;
	}

	public int getRecordLength() {
		return recordLength;
	}

	public void setRecordLength(int recordLength) {
		this.recordLength = recordLength;
	}

	public int getCategoryLength(int category) {
		return categoryLength[category];
	}
}
