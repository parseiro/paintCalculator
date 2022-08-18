import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IParede } from '../parede.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../parede.test-samples';

import { ParedeService } from './parede.service';

const requireRestSample: IParede = {
  ...sampleWithRequiredData,
};

describe('Parede Service', () => {
  let service: ParedeService;
  let httpMock: HttpTestingController;
  let expectedResult: IParede | IParede[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ParedeService);
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

    it('should create a Parede', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const parede = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(parede).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Parede', () => {
      const parede = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(parede).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Parede', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Parede', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Parede', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addParedeToCollectionIfMissing', () => {
      it('should add a Parede to an empty array', () => {
        const parede: IParede = sampleWithRequiredData;
        expectedResult = service.addParedeToCollectionIfMissing([], parede);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(parede);
      });

      it('should not add a Parede to an array that contains it', () => {
        const parede: IParede = sampleWithRequiredData;
        const paredeCollection: IParede[] = [
          {
            ...parede,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addParedeToCollectionIfMissing(paredeCollection, parede);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Parede to an array that doesn't contain it", () => {
        const parede: IParede = sampleWithRequiredData;
        const paredeCollection: IParede[] = [sampleWithPartialData];
        expectedResult = service.addParedeToCollectionIfMissing(paredeCollection, parede);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(parede);
      });

      it('should add only unique Parede to an array', () => {
        const paredeArray: IParede[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const paredeCollection: IParede[] = [sampleWithRequiredData];
        expectedResult = service.addParedeToCollectionIfMissing(paredeCollection, ...paredeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const parede: IParede = sampleWithRequiredData;
        const parede2: IParede = sampleWithPartialData;
        expectedResult = service.addParedeToCollectionIfMissing([], parede, parede2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(parede);
        expect(expectedResult).toContain(parede2);
      });

      it('should accept null and undefined values', () => {
        const parede: IParede = sampleWithRequiredData;
        expectedResult = service.addParedeToCollectionIfMissing([], null, parede, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(parede);
      });

      it('should return initial array if no Parede is added', () => {
        const paredeCollection: IParede[] = [sampleWithRequiredData];
        expectedResult = service.addParedeToCollectionIfMissing(paredeCollection, undefined, null);
        expect(expectedResult).toEqual(paredeCollection);
      });
    });

    describe('compareParede', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareParede(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareParede(entity1, entity2);
        const compareResult2 = service.compareParede(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareParede(entity1, entity2);
        const compareResult2 = service.compareParede(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareParede(entity1, entity2);
        const compareResult2 = service.compareParede(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
