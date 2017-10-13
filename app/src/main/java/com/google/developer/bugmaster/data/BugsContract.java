package com.google.developer.bugmaster.data;

import android.provider.BaseColumns;

/**
 * Created by Sushant.Patekar on 10/11/2017.
 */

public class BugsContract {

    public static final class BugEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "insects";
        public static final String COLUMN_NAME = "friendlyName";
        public static final String COLUMN_SCIENTIFICNAME = "scientificName";
        public static final String COLUMN_CLASSIFICATION = "classification";
        public static final String COLUMN_IMAGEASSET = "imageAsset";
        public static final String COLUMN_DANGERLEVEL = "dangerLevel";
    }
}
