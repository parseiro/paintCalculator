import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISala } from '../sala.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../sala.test-samples';

import { SalaService } from './sala.service';

const requireRestSample: ISala = {
  ...sampleWithRequiredData,
};

describe('Sala Service', () => {
  let service: SalaService;
  let httpMock: HttpTestingController;
  let expectedResult: ISala | ISala[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SalaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Sala', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const sala = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sala).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Sala', () => {
      const sala = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sala).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Sala', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Sala', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Sala', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSalaToCollectionIfMissing', () => {
      it('should add a Sala to an empty array', () => {
        const sala: ISala = sampleWithRequiredData;
        expectedResult = service.addSalaToCollectionIfMissing([], sala);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sala);
      });

      it('should not add a Sala to an array that contains it', () => {
        const sala: ISala = sampleWithRequiredData;
        const salaCollection: ISala[] = [
          {
            ...sala,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSalaToCollectionIfMissing(salaCollection, sala);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Sala to an array that doesn't contain it", () => {
        const sala: ISala = sampleWithRequiredData;
        const salaCollection: ISala[] = [sampleWithPartialData];
        expectedResult = service.addSalaToCollectionIfMissing(salaCollection, sala);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sala);
      });

      it('should add only unique Sala to an array', () => {
        const salaArray: ISala[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const salaCollection: ISala[] = [sampleWithRequiredData];
        expectedResult = service.addSalaToCollectionIfMissing(salaCollection, ...salaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sala: ISala = sampleWithRequiredData;
        const sala2: ISala = sampleWithPartialData;
        expectedResult = service.addSalaToCollectionIfMissing([], sala, sala2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sala);
        expect(expectedResult).toContain(sala2);
      });

      it('should accept null and undefined values', () => {
        const sala: ISala = sampleWithRequiredData;
        expectedResult = service.addSalaToCollectionIfMissing([], null, sala, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sala);
      });

      it('should return initial array if no Sala is added', () => {
        const salaCollection: ISala[] = [sampleWithRequiredData];
        expectedResult = service.addSalaToCollectionIfMissing(salaCollection, undefined, null);
        expect(expectedResult).toEqual(salaCollection);
      });
    });

    describe('compareSala', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSala(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSala(entity1, entity2);
        const compareResult2 = service.compareSala(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSala(entity1, entity2);
        const compareResult2 = service.compareSala(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSala(entity1, entity2);
        const compareResult2 = service.compareSala(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
