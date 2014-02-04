package app.utils;

import utils.LoggingManager;
import utils.PropertyManager;

/**
 *
 * @author Nate
 */
public class RandomGenerator
{

    /**
     * The first random seed
     */
    public static int seed1;

    /**
     * The second random seed
     */
    public static int seed2;

    private static RandomGenerator instance;
    private double u[] = new double[97], c, cd, cm;
    private int i97, j97;
    private boolean init = false;

    /**
     *
     * @return
     */
    public double getRandomNumber()
    {
        return randomUniform();
    }

    /**
     *
     * @return
     */
    public double randomUniform()
    {
        double uni;

        /* Make sure the initialization routine has been called */
        if (!init)
        {
            randomInit(seed1, seed2);
        }

        uni = u[i97 - 1] - u[j97 - 1];
        if (uni <= 0.0)
        {
            uni++;
        }
        u[i97 - 1] = uni;
        i97--;
        if (i97 == 0)
        {
            i97 = 97;
        }
        j97--;
        if (j97 == 0)
        {
            j97 = 97;
        }
        c -= cd;
        if (c < 0.0)
        {
            c += cm;
        }
        uni -= c;
        if (uni < 0.0)
        {
            uni++;
        }

        return (uni);
    }

    /**
     *
     * @return
     */
    public static RandomGenerator Instance()
    {
        if (instance == null)
        {
            if ((seed1 == 0) && (seed2 == 0))
            {
                seed1 = PropertyManager.Instance().getIntProperty("RANDOMSEED1");
                seed2 = PropertyManager.Instance().getIntProperty("RANDOMSEED2");
            }
            instance = new RandomGenerator(seed1, seed2);
        }
        return instance;
    }

    private RandomGenerator(int ij, int kl)
    {
        randomInit(ij, kl);
    }

    /**
     *
     * @param ij
     * @param kl
     */
    public void randomInit(int ij, int kl)
    {
        double s, t;
        int ii, i, j, k, l, jj, m;

        LoggingManager.logInfo("Initializing RandomGenerator using seeds: " + ij + ", " + kl);

        /*
           Handle the seed range errors
           First random number seed must be between 0 and 31328
           Second seed must have a value between 0 and 30081
        */
        if (ij < 0 || ij > 31328 || kl < 0 || kl > 30081)
        {
            ij = 1802;
            kl = 9373;
        }

        i = (ij / 177) % 177 + 2;
        j = (ij % 177) + 2;
        k = (kl / 169) % 178 + 1;
        l = (kl % 169);

        for (ii = 0; ii < 97; ii++)
        {
            s = 0.0;
            t = 0.5;
            for (jj = 0; jj < 24; jj++)
            {
                m = (((i * j) % 179) * k) % 179;
                i = j;
                j = k;
                k = m;
                l = (53 * l + 1) % 169;
                if (((l * m % 64)) >= 32)
                {
                    s += t;
                }
                t *= 0.5;
            }
            u[ii] = s;
        }

        c = 362436.0 / 16777216.0;
        cd = 7654321.0 / 16777216.0;
        cm = 16777213.0 / 16777216.0;
        i97 = 97;
        j97 = 33;
        init = true;
    }

}