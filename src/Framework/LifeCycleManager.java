/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in Myungji University.
 */
package Framework;

import Components.AddFilter.AddFilter;
import Components.Middle.TempMiddleFilter;
import Components.Middle.MiddleFilter;
import Components.Sink.SinkFilter;
import Components.Source.SourceFilter;

public class LifeCycleManager {
    public static void main(String[] args) {
        try {
            CommonFilter sourceFilter = new SourceFilter("Students.txt");
            CommonFilter outputFilter = new SinkFilter("Output.txt");
            CommonFilter tempMiddleFilter = new TempMiddleFilter();
            CommonFilter middleFilter = new MiddleFilter();
            CommonFilter addFilter = new AddFilter();
            
            sourceFilter.connectOutputTo(tempMiddleFilter);
            tempMiddleFilter.connectOutputTo(middleFilter);
            middleFilter.connectOutputTo(addFilter);
            addFilter.connectOutputTo(outputFilter);
            
            Thread thread1 = new Thread(sourceFilter);
            Thread thread2 = new Thread(outputFilter);
            Thread thread3 = new Thread(tempMiddleFilter);
            Thread thread4 = new Thread(middleFilter);
            Thread thread5 = new Thread(addFilter);
            
            thread1.start();
            thread2.start();
            thread3.start();
            thread4.start();
            thread5.start();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
