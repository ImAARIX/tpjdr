import { saveNewMonster, getMonsterById, wasAttacked, MonsterTypeEnum } from './MonsterApi';
import api from './api';

jest.mock('./api');

describe('MonsterApi', () => {
    afterEach(() => {
        jest.clearAllMocks();
    });

    it('saves a new monster successfully', async () => {
        const mockResponse = { data: { id: 1, type: MonsterTypeEnum.RAT, health: 100, droppedObject: { id: 1, inventory: {}, name: 'Rat Tail', characteristics: {} } } };
        (api.get as jest.Mock).mockResolvedValue(mockResponse);

        const result = await saveNewMonster(MonsterTypeEnum.RAT);

        expect(api.get).toHaveBeenCalledWith('/monster/save/RAT');
        expect(result).toEqual(mockResponse.data);
    });

    it('gets a monster by id successfully', async () => {
        const mockResponse = { data: { id: 1, type: MonsterTypeEnum.WOLF, health: 200, droppedObject: { id: 2, inventory: {}, name: 'Wolf Fur', characteristics: {} } } };
        (api.get as jest.Mock).mockResolvedValue(mockResponse);

        const result = await getMonsterById(1);

        expect(api.get).toHaveBeenCalledWith('/monster/1');
        expect(result).toEqual(mockResponse.data);
    });

    it('returns null if monster was not attacked', async () => {
        (api.put as jest.Mock).mockResolvedValue({ data: null });

        const result = await wasAttacked(1, 'user123');

        expect(api.put).toHaveBeenCalledWith('/monster/1/attacked/user123');
        expect(result).toBeNull();
    });

    it('returns monster response if monster was attacked', async () => {
        const mockResponse = { data: { id: 1, type: MonsterTypeEnum.DEMON, health: 50, droppedObject: { id: 3, inventory: {}, name: 'Demon Horn', characteristics: {} } } };
        (api.put as jest.Mock).mockResolvedValue(mockResponse);

        const result = await wasAttacked(1, 'user123');

        expect(api.put).toHaveBeenCalledWith('/monster/1/attacked/user123');
        expect(result).toEqual(mockResponse.data);
    });

    it('handles error when saving a new monster', async () => {
        (api.get as jest.Mock).mockRejectedValue(new Error('Network Error'));

        await expect(saveNewMonster(MonsterTypeEnum.RAT)).rejects.toThrow('Network Error');
    });

    it('handles error when getting a monster by id', async () => {
        (api.get as jest.Mock).mockRejectedValue(new Error('Network Error'));

        await expect(getMonsterById(1)).rejects.toThrow('Network Error');
    });

    it('handles error when checking if monster was attacked', async () => {
        (api.put as jest.Mock).mockRejectedValue(new Error('Network Error'));

        await expect(wasAttacked(1, 'user123')).rejects.toThrow('Network Error');
    });
});
