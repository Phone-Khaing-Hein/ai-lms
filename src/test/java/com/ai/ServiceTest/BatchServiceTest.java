package com.ai.ServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.entity.Batch;
import com.ai.repository.BatchRepository;
import com.ai.service.BatchService;

@SpringBootTest
public class BatchServiceTest {
    
    @Mock
    BatchRepository batchRepository;

    @InjectMocks
    BatchService batchService;

    public Batch batchObj(){
        Batch batch = Batch.builder()
        .id(1)
        .name("Batch-1")
        .startDate(LocalDate.of(2022, 9, 23))
        .endDate(LocalDate.of(2022, 12, 6))
        .build();
        return batch;
    }

    public List<Batch> batchList(){
        List<Batch> batchList = new ArrayList<Batch>();
        Batch batch1 = Batch.builder()
        .id(1)
        .name("Batch-1")
        .startDate(LocalDate.of(2022, 9, 23))
        .endDate(LocalDate.of(2022, 12, 6))
        .build();

        Batch batch2 = Batch.builder()
        .id(1)
        .name("Batch-1")
        .startDate(LocalDate.of(2022, 9, 23))
        .endDate(LocalDate.of(2022, 12, 6))
        .build();
        batchList.add(batch1);
        batchList.add(batch2);
        return batchList;
    }
    @Test
    public void saveBatchTest(){
        Batch batch = batchObj();
        batchService.save(batch);
        verify(batchRepository, times(1)).save(batch);
    }

    @Test
    public void findByNameTest(){
        Batch batch = batchObj();
        Mockito.when(batchRepository.findByName("Batch-1")).thenReturn(batch);
        Batch getBatch = batchService.findByName("Batch-1");
        assertEquals(1, getBatch.getId());
        assertEquals(LocalDate.of(2022, 9, 23), getBatch.getStartDate());
        assertEquals(LocalDate.of(2022, 12, 6), getBatch.getEndDate());
    }

    @Test
    public void findAllTest(){
        List<Batch> batches = batchList();
        Mockito.when(batchRepository.findAll()).thenReturn(batches);
        List<Batch> batchList = batchService.findAll();
        assertEquals(2, batchList.size());
        verify(batchRepository, times(1)).findAll();
    }

    @Test
    public void findByIdTest(){
        Batch batch = batchObj();
        Mockito.when(batchRepository.findById(1)).thenReturn(Optional.of(batch));
        Batch getBatch = batchService.findById(1);
        assertEquals("Batch-1", getBatch.getName());
        assertEquals(LocalDate.of(2022, 9, 23), getBatch.getStartDate());
        assertEquals(LocalDate.of(2022, 12, 6), getBatch.getEndDate());
    }

    @Test
	public void deleteTest() {
		batchService.deleteById(1);
		verify(batchRepository,times(1)).deleteById(1);
	}

    @Test
    public void getCountTest(){
        int serviceCount = batchService.getCount();
        int repositoryCount = (int) batchRepository.count();
        assertEquals(serviceCount, repositoryCount);
    }
}
