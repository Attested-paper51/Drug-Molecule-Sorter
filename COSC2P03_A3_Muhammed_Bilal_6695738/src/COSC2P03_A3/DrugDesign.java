package COSC2P03_A3;
// Imports
import java.io.*;

public class DrugDesign
{
    // Instance Variables
    String path = "C:\\Users\\Admin\\Desktop\\COSC 2P03\\ASSIGNMENTS\\ASSIGNMENT#3\\COSC2P03_Assignment3\\ZINCSubet.csv";
    // put in while loop   // created an instance of the DataItem class
    public DataItem [] originalData = new DataItem [12000];
    public DataItem [] finalSortedData = new DataItem [12000];

    public float [] HeapLogp = new float [12000];
    public int [] IndexArr = new int [HeapLogp.length];

    public float [] mr = new float [12000];
    public int [] mrIndexArr = new int [mr.length];

    public float [] SAS = new float [12000];
    public float [] tempSAS = new float[SAS.length];
    public float [] SASArr = new float [SAS.length];
    public int [] SASIndexArr = new int [SAS.length];



    // Constructor
    public DrugDesign() throws IOException
    {
        loadData(path);

        // Heap Sort
        for(int i = 0; i < IndexArr.length; i++)
        {
            IndexArr[i] = i;
        }
        heapSort(HeapLogp);
        System.out.println("------------------------------------------------------Heap Sort: logp--------------------------------------------------");
        printArray(IndexArr);

        // Quick Sort
        for(int i = 0; i < mrIndexArr.length; i++)
        {
            mrIndexArr[i] = i;
        }
        for(int i = 0; i < mr.length; i++)
        {
            mr[i] = originalData[i].mr;
        }
        QuickSort(mr, 0, mr.length - 1);
        System.out.println("------------------------------------------------------Quick Sort: mr--------------------------------------------------");
        printArray(mrIndexArr);

        // Merge Sort
        for(int i = 0; i < SAS.length; i++)
        {
            SAS[i] = originalData[i].SAS;
            SASArr[i] = SAS[i];
        }
        Mergesort(SAS, tempSAS, 0, SAS.length - 1);
        IndexArr();
        System.out.println("------------------------------------------------------Merge Sort: SAS--------------------------------------------------");
        printArray(SASIndexArr);

        // SumRankSort
        int[] sortedIndexFinal = sumRankSort(IndexArr, mrIndexArr, SASIndexArr);
        System.out.println("------------------------------------------------------SumRankSort Sort:--------------------------------------------------");
        printArray(sortedIndexFinal);

        SaveNewData(sortedIndexFinal);
    }

    // Methods
    public void loadData(String path) throws IOException
    {
        String line1 = "";
        int i = 0;
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(path));
            System.out.println(br.readLine());
            while((line1 = br.readLine()) != null)
            {
                DataItem item = new DataItem();
                String[] ar = line1.split(",");

                item.smiles = ar[0];
                //System.out.print(item.smiles);
                item.fragments = ar[1];
                //System.out.print(", " + item.fragments);
                item.n_fragments = Integer.parseInt(ar[2]);
                //System.out.print(", " + item.n_fragments);
                item.C = Integer.parseInt(ar[3]);
                //System.out.print(", " + item.C);
                item.F = Integer.parseInt(ar[4]);
                //System.out.print(", " + item.F);
                item.N = Integer.parseInt(ar[5]);
                //System.out.print(", " + item.N);
                item.O = Integer.parseInt(ar[6]);
                //System.out.print(", " + item.O);
                item.Other = Integer.parseInt(ar[7]);
                //System.out.print(", " + item.Other);
                item.SINGLE = Integer.parseInt(ar[8]);
                //System.out.print(", " + item.SINGLE);
                item.DOUBLE = Integer.parseInt(ar[9]);
                //System.out.print(", " + item.DOUBLE);
                item.TRIPLE = Integer.parseInt(ar[10]);
                //System.out.print(", " + item.TRIPLE);
                item.Tri = Integer.parseInt(ar[11]);
               // System.out.print(", " + item.Tri);
                item.Quad = Integer.parseInt(ar[12]);
                //System.out.print(", " + item.Quad);
                item.Pent = Integer.parseInt(ar[13]);
                //System.out.print(", " + item.Pent);
                item.Hex = Integer.parseInt(ar[14]);
                //System.out.print(", " + item.Hex);
                item.logP = Float.parseFloat(ar[15]);
                //System.out.print(", " + item.logP);
                item.mr = Float.parseFloat(ar[16]);
                //System.out.print(", " + item.mr);
                item.qed = Float.parseFloat(ar[17]);
                //System.out.print(", " + item.qed);
                item.SAS = Float.parseFloat(ar[18]);
                //System.out.print(", " + item.SAS);

                originalData[i] = item;
                //System.out.println(originalData[i].smiles + " , " + originalData[i].fragments + " , " + originalData[i].n_fragments + " , " + originalData[i].C + " , " + originalData[i].F + " , " + originalData[i].N + " , " + originalData[i].O + " , " + originalData[i].Other + " , " + originalData[i].SINGLE + " , " + originalData[i].DOUBLE + " , " + originalData[i].TRIPLE + " , " + originalData[i].Tri + " , " + originalData[i].Quad + " , " + originalData[i].Pent + " , " + originalData[i].Hex + " , " + originalData[i].logP + " , " + originalData[i].mr + " , " + originalData[i].qed + " , " + originalData[i].SAS);
                i++;

                //break;
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public int [] heapSort(float [] HeapLogp)
    {
        for(int i = 0; i < HeapLogp.length; i++)
        {
            HeapLogp[i] = originalData[i].logP;
            //System.out.println(HeapLogp[i]);
            //System.out.println(IndexArr[i]);
        }

        // Build heap
        for (int i = HeapLogp.length; i >= 0; i--)
        {
            heapify(HeapLogp, HeapLogp.length, i);
        }

        // seperate the largest element by moving it to the end of the array
        for (int i = HeapLogp.length - 1; i > 0; i--)
        {
            // Move current root to end
            float temp = HeapLogp[0];
            HeapLogp[0] = HeapLogp[i];
            HeapLogp[i] = temp;

            int temp2 = IndexArr[0];
            IndexArr[0] = IndexArr[i];
            IndexArr[i] = temp2;

            // call the max heap on the reduced heap
            heapify(HeapLogp, i, 0);
        }
        return IndexArr;
    }

    // Heapifies the array by recursively labelling which of the 3: root, left, right - is the largest
    // and then swapping the largest with the root
    public void heapify(float HeapLogp[], int n, int i)
    {
        int largest = i; // root is initially labelled as the largest, unless the children are larger
        int l = 2 * i + 1; // left child
        int r = 2 * i + 2; // right chile
        // If the left child is larger than the root
        if (l < n && HeapLogp[l] > HeapLogp[largest])
            largest = l;

        // If the right child is larger than largest so far : could be the root or left child
        if (r < n && HeapLogp[r] > HeapLogp[largest])
            largest = r;

        // If the largest is not the root anymore
        if (largest != i)
        {
            float swap = HeapLogp[i];
            HeapLogp[i] = HeapLogp[largest];
            HeapLogp[largest] = swap;

            int swap2 = IndexArr[i];
            IndexArr[i] = IndexArr[largest];
            IndexArr[largest] = swap2;

            // Recursively heapify the array
            heapify(HeapLogp, n, largest);
        }
    }

    /* The main function that implements QuickSort
       float arr[] - is the Array to be sorted
       low - the first index (0)
       high - the last index (arr.length - 1)
  */
    public void QuickSort(float[] arr, int low, int high)
    {
        if (low < high)
        {
            // pivot is partitioning the left and right side apart
            int pivot = partition(arr, low, high);

            // Separately sort elements on left and right side of the pivot
            QuickSort(arr, low, pivot - 1);
            QuickSort(arr, pivot + 1, high);
        }
    }

    /*
    * Takes the last element as a pivot value
    * Then places it in its sorted position
    * partitions the left - smaller than the pivot and right - larger than the pivot*/
    public int partition(float[] arr, int low, int high)
    {
        // pivot
        float pivot = arr[high];
        int i = (low - 1);

        for(int j = low; j <= high - 1; j++)
        {
            // If current element is smaller
            // than the pivot
            if (arr[j] < pivot)
            {
                // Increment index of
                // smaller element
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return (i + 1);
    }

    // swaps two elements in a float array
    public void swap(float[] arr, int i, int j)
    {
        float temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;

        // swaps the indicies as well
        int temp2 = mrIndexArr[i];
        mrIndexArr[i] = mrIndexArr[j];
        mrIndexArr[j] = temp2;
    }


    public void Merge(float [] A, float [] tmp , int leftPos , int rightPos , int rightEnd)
    {
        int leftEnd = rightPos - 1;
        int tmpPos = leftPos;
        int numElements = rightEnd - leftPos + 1;
        while(leftPos <= leftEnd && rightPos <= rightEnd)
        {
            if(A[leftPos] <= A[rightPos])
            {
                //tmp[tmpPos] = SASIndexArr[leftPos];
                tmp[tmpPos] = A[leftPos];
                tmpPos++;
                leftPos++;
            }
            else
            {
                //tmp[tmpPos] = SASIndexArr[rightPos];
                tmp[tmpPos] = A[rightPos];
                tmpPos++;
                rightPos++;
            }
        }
        while(leftPos <= leftEnd)
        {
            //tmp[tmpPos] =  SASIndexArr[leftPos];
            tmp[tmpPos] = A[leftPos];
            tmpPos++;
            leftPos++;
        }
        while(rightPos <= rightEnd)
        {
            //tmp[tmpPos] =  SASIndexArr[rightPos];
            tmp[tmpPos] = A[rightPos];
            tmpPos++;
            rightPos++;
        }
        for(int i = 0; i < numElements ; i++, rightEnd--)
        {
            A[rightEnd] = tmp[rightEnd];
            //SASIndexArr[rightEnd] = (int) tmp[rightEnd];
            //System.out.println(tmp[rightEnd]);
        }
    }

    public void Mergesort(float [] A, float [] tmp, int lower, int upper)
    {
        if(lower < upper)
        {
            int mid = (lower+upper)/2; //int division
            Mergesort(A, tmp, lower, mid);
            Mergesort(A, tmp, mid+1, upper);
            Merge(A, tmp, lower, mid+1, upper);
        }
    }

    /*
    Takes the 3 1-d sorted index arrays and adds all the indicies together into each index of another array
    sorts the new array using heap sort
    returns as an int [] array     */
    public int [] sumRankSort(int [] logpArr, int [] mrArr, int [] SASArr)
    {
        float [] sumrank = new float[logpArr.length];
        for(int i = 0; i < logpArr.length; i++)
        {
            sumrank[i] = logpArr[i] + mrArr[i] + SASArr[i];
            //System.out.println(sumrank[i]);
        }
        int [] sumranksort = heapSort(sumrank);
        return sumranksort;
    }

    /* Prints an int [] array - used to print out the index arrays */
    public void printArray(int arr[])
    {
        for (int i = 0; i < arr.length; ++i)
        {
            System.out.print(arr[i] + " , ");
        }
        System.out.println();
        System.out.println();
    }

    /*
    Used for the merge sort algorithm to effectively sort the array based on indicies
    takes the original un-sorted array
    compares value stored in 1st index to each index of the sorted array
    when values match, then inserts the index value into the sorted array
     */
    public void IndexArr()
    {
        for(int i = 0; i < SASArr.length; i++)
        {
            for(int j = 0; j < SAS.length; j++)
            {
                if(SASArr[i] == SAS[j])
                {
                    SASIndexArr[j] = i;
                }
            }
        }
    }

    /*
    Uses PrintWriter to create a CSV file of the object "finalSortedData"
     */
    public void SaveNewData(int [] sortedIndexFinal)
    {
        try {
            PrintWriter pw = new PrintWriter(new File("C:\\Users\\Admin\\Desktop\\COSC 2P03\\ASSIGNMENTS\\ASSIGNMENT#3\\COSC2P03_Assignment3\\ZINCSubet_sorted.csv"));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < sortedIndexFinal.length; i++)
            {
                DataItem item2 = new DataItem();
                item2.sumRank = sortedIndexFinal[i];
                finalSortedData[i] = item2;
                sb.append(finalSortedData[i].sumRank);
                sb.append("\n");
            }
            pw.write(sb.toString());
            pw.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException
    {
        DrugDesign d = new DrugDesign();
    }
}
