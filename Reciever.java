public class BootReciever extends BroadcastReceiver
{

  @Override
  public void onReceive(Context context, Intent intent)
  {
    // TODO Auto-generated method stub
    Intent myIntent = new Intent(context, Tabs.class);
    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(myIntent);
  }

}
