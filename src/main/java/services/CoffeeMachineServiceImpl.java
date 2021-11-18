package services;

import lombok.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Beverage;
import models.InputData;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Data
public class CoffeeMachineServiceImpl implements CoffeeMachineService{

    private Map<String, Long> itemsInMachine;
    private CyclicBarrier gate;

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock writeLock = lock.writeLock();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void prepareBeverage(InputData inputData) {
        int outLetCount = inputData.getMachine().getOutlets().getCountN();
        setItemsInMachine(objectMapper.convertValue(inputData.getMachine().getTotalItemsQuantity(), Map.class));
        setGate(new CyclicBarrier(outLetCount + 1));

        List<Thread> threads = new LinkedList<>();
        Map<String, Beverage> beveragesMap = inputData.getMachine().getBeverages();
        int count = 0;
        for(String beverage:beveragesMap.keySet())    {
            Thread t = new Thread(() -> {
                try {
                    gate.await();
                    prepareBeverage(beverage, objectMapper.convertValue(beveragesMap.get(beverage), Map.class));
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
            threads.add(t);
            count++;
            if(count % outLetCount != 0 && count == beveragesMap.size())
                setGate(new CyclicBarrier(count % outLetCount));
            if(count % outLetCount == 0 || count == beveragesMap.size()){
                try {
                    for (Thread thread: threads)
                        thread.start();
                    gate.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                threads = new LinkedList<>();
            }
        }
    }

    public void prepareBeverage(String beverage, Map<String, Long> beverageIngredientMap) {
        for(String ingredient: beverageIngredientMap.keySet())   {
            int res = consumeIngredient(ingredient, beverageIngredientMap.get(ingredient));
            if(res == -1)  {
                System.out.println(beverage + " cannot be prepared because "+ingredient+" is not available");
                return;
            }
            else if(res == 0)    {
                System.out.println(beverage + " cannot be prepared because "+ingredient+" is not sufficient");
                return;
            }
        }
        System.out.println(beverage + " is prepared");
    }

    private int consumeIngredient(String ingredient, Long amount)  {
        try {
            writeLock.lock();
            if(!itemsInMachine.containsKey(ingredient))
                return -1;
            if(itemsInMachine.get(ingredient) < amount)
                return 0;
            itemsInMachine.put(ingredient, itemsInMachine.get(ingredient) - amount);
            return 1;
        } finally {
            writeLock.unlock();
        }
    }
}
