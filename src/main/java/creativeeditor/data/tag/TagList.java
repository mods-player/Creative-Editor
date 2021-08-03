package creativeeditor.data.tag;

import com.google.common.collect.Lists;
import creativeeditor.data.Data;
import creativeeditor.data.base.SingularData;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

public class TagList<E extends Data<?, ?>> extends SingularData<List<E>, ListNBT> implements Iterable<E> {
    protected Function<INBT, E> addFunction;


    public TagList(Function<INBT, E> addFunction) {
        this(Lists.newArrayList());
        this.addFunction = addFunction;
    }


    public TagList(ListNBT nbt, Function<INBT, E> addFunction) {
        this(addFunction);
        nbt.forEach(this::add);
    }


    public TagList(List<E> list) {
        super(list);
    }


    public void add(E value) {
        if (value != null)
            data.add(value);
    }


    public void add(INBT nbt) {
        add(addFunction.apply(nbt));
    }

    public void remove(E value) {
        data.remove(value);
    }

    public void remove(int index) {
        data.remove(index);
    }


    public void clear() {
        data.clear();
    }


    @Override
    public boolean isDefault() {
        return data.isEmpty();
    }


    @Override
    public ListNBT getNBT() {
        ListNBT nbt = new ListNBT();
        data.forEach(dat -> {
            if (!dat.isDefault()) {
                nbt.add(dat.getNBT());
            }
        });
        return nbt;
    }


    @Override
    public ListIterator<E> iterator() {
        return data.listIterator();
    }
}
