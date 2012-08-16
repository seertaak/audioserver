/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.sillytest;

/**
 *
 * @author Martin Percossi
 */
public class FinalTest1 {

    public static interface IFoo {

        float calcSampleIface(float left, float right);
    }

    public final static class Foo implements IFoo {

        @Override
        public final float calcSampleIface(final float left, final float right) {
            return 0.5f * (left + right) * (left + right);
        }
    }
    
    public final static class Foo2  {

        public final float calcSampleVirtual(final float left, final float right) {
            return 0.5f * (left + right) * (left + right);
        }

        public final static float calcSampleStatic(final float left,
                final float right) {
            return 0.5f * (left + right) * (left + right);
        }
    }
    
    public final static class Foo3  {

        public final static float calcSampleStatic(final float left,
                final float right) {
            return 0.5f * (left + right) * (left + right);
        }
        
        public void foobar() {
            
        }
    }

    public static void main(String... args) {
        int N = 5000000;
        float[] array = new float[N];
        for (int i = 0; i < N; i++) {
            array[i] = (float) Math.random();
        }
        
        for (int run = 0; run < 10; run++) {
            System.out.println("----");
            System.out.println("Run: " + run);

            long t = System.currentTimeMillis();

            float total = 0;
            float left;
            float right;
            for (int count = 0; count < 500; count++) {
                float sum = 0f;
                for (int i = 0; i < N / 2; i++) {
                    left = array[i];
                    right = array[i+1];
                    sum += 0.5f * (left + right) * (left + right);
                }
                total += sum;
            }

            //System.out.println("Total: " + total);
            long T = System.currentTimeMillis();
            double dT = T - t;
            dT /= 1000.0;
            double dT0 = dT;
            System.out.println("Time : " + dT);


            t = System.currentTimeMillis();
            IFoo foo = new Foo();
            total = 0;
            for (int count = 0; count < 500; count++) {
                float sum = 0f;
                for (int i = 0; i < N / 2; i++) {
                    sum += foo.calcSampleIface(array[i], array[i + 1]);
                }
                total += sum;
            }

            System.out.println("Interface:");
            //System.out.println("Total: " + total);
            T = System.currentTimeMillis();
            dT = T - t;
            dT /= 1000.0;
            System.out.println("Time : " + dT);

            System.out.println("Time factor: " + (dT / dT0));


            t = System.currentTimeMillis();
            Foo2 foo2 = new Foo2();
            total = 0;
            for (int count = 0; count < 500; count++) {
                float sum = 0f;
                for (int i = 0; i < N / 2; i++) {
                    sum += foo2.calcSampleVirtual(array[i], array[i + 1]);
                }
                total += sum;
            }

            System.out.println("Virtual:");
            //System.out.println("Total: " + total);
            T = System.currentTimeMillis();
            dT = T - t;
            dT /= 1000.0;
            System.out.println("Time : " + dT);

            System.out.println("Time factor: " + (dT / dT0));


            t = System.currentTimeMillis();
            total = 0;
            for (int count = 0; count < 500; count++) {
                float sum = 0f;
                for (int i = 0; i < N / 2; i++) {
                    sum += Foo3.calcSampleStatic(array[i], array[i + 1]);
                }
                total += sum;
            }

            System.out.println("Static:");
            //System.out.println("Total: " + total);
            T = System.currentTimeMillis();
            dT = T - t;
            dT /= 1000.0;
            System.out.println("Time : " + dT);

            System.out.println("Time factor: " + (dT / dT0));
        }
    }
}
